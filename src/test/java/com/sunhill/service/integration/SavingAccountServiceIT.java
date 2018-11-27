package com.sunhill.service.integration;

import com.sunhill.entity.AccountFactory;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.exception.UnsuportedAccountTypeException;
import com.sunhill.service.SavingAccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;


public class SavingAccountServiceIT {

    private final double INTEREST_RATE = 0.03;
    private final double INITIAL_BALANCE = 1000.0;
    private final long USER_ID = 11L;

    private SavingAccountService savingAccountService;
    private long accountId;

    @Before
    public void setUp() throws IllegalValueException, UnsuportedAccountTypeException {
        savingAccountService = Mockito.spy(new SavingAccountService());
        AccountFactory factory = new AccountFactory.Builder(USER_ID, INITIAL_BALANCE)
                .interestRate(INTEREST_RATE).build();
        MockitoAnnotations.initMocks(this);

        this.accountId = savingAccountService.createAccount(factory);
    }

    @Test
    public void shouldPayInterest() throws IllegalValueException, NotFoundException {
        double mockedPayment = 60.0;
        Mockito.when(savingAccountService.calculateInterest(accountId)).thenReturn(mockedPayment);

        double newBalance = savingAccountService.payInterest(accountId);

        assertEquals(INITIAL_BALANCE + mockedPayment, newBalance);
    }

    @Test(expected = IllegalValueException.class)
    public void shouldNotPayInterestIfPaymentIsNegative() throws IllegalValueException, NotFoundException {
        double mockedPayment = -60.0;
        Mockito.when(savingAccountService.calculateInterest(accountId)).thenReturn(mockedPayment);

        savingAccountService.payInterest(accountId);
    }

    @Test
    public void shouldCalculateInterest() throws IllegalValueException, NotFoundException {
        double interest = savingAccountService.calculateInterest(accountId);

        assertEquals(INITIAL_BALANCE * INTEREST_RATE, interest);
    }

    @Test
    public void shouldDeposit() throws IllegalValueException, NotFoundException {
        double increment = 5.0;
        double newBalance = savingAccountService.deposit(accountId, increment);

        assertEquals(INITIAL_BALANCE + increment, newBalance);
    }

    @Test(expected = IllegalValueException.class)
    public void shouldNotDepositIfIncrementIsNegative() throws IllegalValueException, NotFoundException {
        double increment = -55.0;
        savingAccountService.deposit(USER_ID, increment);
    }

    @Test
    public void shouldWithdraw() throws IllegalValueException, NotFoundException {
        double amount = 5.0;

        double newBalance = savingAccountService.withdraw(accountId, amount);

        assertEquals(INITIAL_BALANCE - amount, newBalance);
    }

    @Test(expected = IllegalValueException.class)
    public void shouldNotWithdrawIfIncrementIsNegative() throws IllegalValueException, NotFoundException {
        double increment = -55.0;
        savingAccountService.withdraw(USER_ID, increment);
    }
}
