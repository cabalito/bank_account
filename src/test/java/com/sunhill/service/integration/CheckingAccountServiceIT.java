package com.sunhill.service.integration;

import com.sunhill.entity.AccountFactory;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.LimitExeceededException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.exception.UnsuportedAccountTypeException;
import com.sunhill.service.CheckingAccountService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class CheckingAccountServiceIT {

    private final double LIMIT_OVERDRAFT = -700;
    private final double INITIAL_BALANCE = 1000.0;
    private final long USER_ID = 11L;

    private CheckingAccountService checkingAccountService;
    private Long accountId;

    @Before
    public void setUp() throws IllegalValueException, UnsuportedAccountTypeException {
        checkingAccountService = new CheckingAccountService();
        AccountFactory factory = new AccountFactory.Builder(USER_ID, INITIAL_BALANCE)
                .limitOverdraft(LIMIT_OVERDRAFT).build();

        accountId = checkingAccountService.createAccount(factory);
    }
    @Test
    public void shouldWithdraw() throws IllegalValueException, NotFoundException {
        double amount = 5.0;

        double newBalance = checkingAccountService.withdraw(accountId, amount);

        assertEquals(INITIAL_BALANCE - amount, newBalance);
    }

    @Test
    public void shouldWithdrawWithNegativeAmountButAmongAllowedLimits() throws IllegalValueException, NotFoundException, UnsuportedAccountTypeException {
        double amount = 50.0;
        double initialBalance = 10.0;
        AccountFactory factory = new AccountFactory.Builder(USER_ID, initialBalance)
                .limitOverdraft(LIMIT_OVERDRAFT).build();
        accountId = checkingAccountService.createAccount(factory);

        double newBalance = checkingAccountService.withdraw(accountId, amount);

        assertEquals(initialBalance - amount, newBalance);
    }

    @Test(expected = LimitExeceededException.class)
    public void shouldNotWithdrawIfNegativeAmontIsMoreThanOverdraftLimit() throws IllegalValueException, NotFoundException {
        double amount = 5500.0;
        checkingAccountService.withdraw(accountId, amount);
    }

    @Test
    public void shouldTransfer() throws IllegalValueException, UnsuportedAccountTypeException, NotFoundException {
        double amount = 50.0;
        Long destinationAccountId = createDestinationAccount();

        double newBalance = checkingAccountService.transfer(accountId, destinationAccountId, amount);

        assertEquals(INITIAL_BALANCE - amount, newBalance);
    }

    private Long createDestinationAccount() throws IllegalValueException, UnsuportedAccountTypeException {
        return checkingAccountService
                .createAccount(
                        new AccountFactory.Builder(USER_ID, INITIAL_BALANCE)
                .limitOverdraft(LIMIT_OVERDRAFT).build());
    }


    @Test
    public void shouldDeposit() throws IllegalValueException, NotFoundException {
        double increment = 5.0;
        double newBalance = checkingAccountService.deposit(accountId, increment);

        assertEquals(INITIAL_BALANCE + increment, newBalance);
    }

    @Test(expected = IllegalValueException.class)
    public void shouldNotDepositIfIncrementIsNegative() throws IllegalValueException, NotFoundException {
        double increment = -55.0;
        checkingAccountService.deposit(accountId, increment);
    }


    @Test(expected = IllegalValueException.class)
    public void shouldNotWithdrawIfIncrementIsNegative() throws IllegalValueException, NotFoundException {
        double increment = -55.0;
        checkingAccountService.withdraw(accountId, increment);
    }
}


