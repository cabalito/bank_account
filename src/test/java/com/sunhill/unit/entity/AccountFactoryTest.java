package com.sunhill.unit.entity;

import com.sunhill.entity.AccountFactory;
import com.sunhill.entity.CheckingAccount;
import com.sunhill.entity.SavingAccount;
import com.sunhill.exception.UnsuportedAccountTypeException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by javierm on 27/11/18.
 */
public class AccountFactoryTest {

    private AccountFactory factory;
    private long owner;
    private double balance;
    private double interestRate;
    private double allowedOverdraft;

    @Before
    public void setUp() throws Exception {
        owner = 12L;
        balance = 2200;
        interestRate = 0.01;
        allowedOverdraft = -200;

        factory = new AccountFactory.Builder(owner, balance)
                .interestRate(interestRate).limitOverdraft(allowedOverdraft).build();
    }

    @Test
    public void shouldCreateCheckingAccount() throws UnsuportedAccountTypeException {
        try {
            CheckingAccount account = (CheckingAccount) factory.createEntity(CheckingAccount.class);
        } catch (ClassCastException e) {
            fail();
        }
    }

    @Test(expected = ClassCastException.class)
    public void shouldFailWithWrongAccount() throws UnsuportedAccountTypeException {
        CheckingAccount account = (CheckingAccount) factory.createEntity(SavingAccount.class);
    }

    @Test
    public void testCreateSavingAccount() throws UnsuportedAccountTypeException {
        try {
            SavingAccount account =
                    (SavingAccount) factory.createEntity(SavingAccount.class);
        } catch (ClassCastException e) {
            fail();
        }
    }

    @Test(expected = UnsuportedAccountTypeException.class)
    public void shouldThrowExceptionWhitUnsuportedType() throws Exception {
        factory.createEntity(String.class);
    }
}