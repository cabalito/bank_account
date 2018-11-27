package com.sunhill.unit.service;

import com.sunhill.entity.SavingAccount;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.exception.UnsuportedAccountTypeException;
import com.sunhill.repository.AccountRepository;
import com.sunhill.service.SavingAccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;


public class SavingAccountServiceTest {


    private SavingAccountService savingAccountService;
    private long accountId;

    private SavingAccount mockAccount;
    private AccountRepository mockRepository;

    @Before
    public void setUp() throws IllegalValueException, UnsuportedAccountTypeException, NotFoundException {
        mockAccount = Mockito.mock(SavingAccount.class);
        mockRepository = Mockito.mock(AccountRepository.class);

        savingAccountService = Mockito.spy(new MockSavingAccountService(mockRepository, mockAccount));
        accountId = 1L;
    }

    @Test
    public void shouldPayInterest() throws IllegalValueException, NotFoundException {
        double interest = 5.0;
        Mockito.doReturn(interest).when(savingAccountService).calculateInterest(accountId);

        savingAccountService.payInterest(accountId);

        Mockito.verify(savingAccountService, Mockito.times(1)).deposit(accountId, interest);
    }

    @Test
    public void shouldCalculateInterest() throws IllegalValueException, NotFoundException {
        double balance = 1000.0;
        double interestRate = 0.03;
        Mockito.doReturn(balance).when(mockAccount).getBalance();
        Mockito.doReturn(interestRate).when(mockAccount).getInterestRate();

        double result = savingAccountService.calculateInterest(accountId);

        assertEquals(balance * interestRate, result);
    }

    @Test(expected = IllegalValueException.class)
    public void shouldThrowExceptionWhenInterestIsNegative() throws IllegalValueException, NotFoundException {
        double balance = 1000.0;
        double interestRate = -0.03;
        Mockito.doReturn(balance).when(mockAccount).getBalance();
        Mockito.doReturn(interestRate).when(mockAccount).getInterestRate();

        savingAccountService.calculateInterest(accountId);

    }

    private class MockSavingAccountService extends SavingAccountService {

        public MockSavingAccountService(AccountRepository repository, SavingAccount account)
                throws NotFoundException {
            this.repository = repository;
            Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(account);
        }
    }
}


