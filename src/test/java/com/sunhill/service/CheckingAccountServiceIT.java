package com.sunhill.service;

import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class CheckingAccountServiceIT {

    private final double INITIAL_BALANCE = 1000.0;

    private CheckingAccountService checkingAccountService;
    private Long accountId;

    @Before
    public void setUp() throws IllegalValueException {
        checkingAccountService = new CheckingAccountService();
        accountId = checkingAccountService.createAccount(INITIAL_BALANCE);
    }
    @Test
    public void shouldWithdraw() throws IllegalValueException, NotFoundException {
        double amount = 5.0;

        double newBalance = checkingAccountService.withdraw(accountId, amount);

        assertEquals(INITIAL_BALANCE - amount, newBalance);
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


