package com.sunhill.concurrency;

import com.sunhill.entity.AccountFactory;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.exception.UnsuportedAccountTypeException;
import com.sunhill.service.CheckingAccountService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TransferCheckingAccountConcurrenceTest {

    private final double LIMIT_OVERDRAFT = -700;
    private final double INITIAL_BALANCE = 1000.0;
    private final long USER_ID = 11L;

    private CheckingAccountService checkingAccountService;
    private Long originAccountId;
    private Long destinationAccountId;

    @Before
    public void setUp() throws IllegalValueException, UnsuportedAccountTypeException {
        checkingAccountService = new CheckingAccountService();
        AccountFactory factory = new AccountFactory.Builder(USER_ID, INITIAL_BALANCE)
                .limitOverdraft(LIMIT_OVERDRAFT).build();

        originAccountId = checkingAccountService.createAccount(factory);
        destinationAccountId = checkingAccountService.createAccount(factory);
    }

    @Test
    public void shouldTransferWithConcurrency() throws NotFoundException {
        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(new Transaction(originAccountId, destinationAccountId, 200));
            Thread t2 = new Thread(new Transaction(destinationAccountId, originAccountId, 150));

            t1.start();
            t2.start();
        }

        double originBalance = checkingAccountService.getBalance(originAccountId);
        double destinationBalance = checkingAccountService.getBalance(destinationAccountId);

        Assert.assertEquals(INITIAL_BALANCE + INITIAL_BALANCE, originBalance + destinationBalance, 0);

    }

    private class Transaction implements Runnable {

        private Long originId;
        private Long destinationId;
        private double amount;

        public Transaction(Long originId, Long destinationId, double amount) {
            this.originId = originId;
            this.destinationId = destinationId;
            this.amount = amount;
        }

        public void run() {

            try {
                checkingAccountService.transfer(originId, destinationId, amount);
            } catch (NotFoundException | IllegalValueException e) {
                Assert.fail();
            }

            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
                Assert.fail();
            }

        }
    }
}


