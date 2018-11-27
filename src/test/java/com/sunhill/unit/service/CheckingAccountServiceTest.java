package com.sunhill.unit.service;

import com.sunhill.entity.CheckingAccount;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.LimitExeceededException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.exception.UnsuportedAccountTypeException;
import com.sunhill.repository.AccountRepository;
import com.sunhill.service.CheckingAccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;


public class CheckingAccountServiceTest {

    private final double INITIAL_BALANCE = 1000.0;

    private CheckingAccountService checkingAccountService;
    private Long accountId;
    double amount = 5.0;


    private CheckingAccount mockAccount;
    private AccountRepository mockRepository;

    @Before
    public void setUp() throws IllegalValueException, UnsuportedAccountTypeException, NotFoundException {
        mockAccount = Mockito.mock(CheckingAccount.class);
        mockRepository = Mockito.mock(AccountRepository.class);

        checkingAccountService = Mockito.spy(new MockCheckingAccountService(mockRepository, mockAccount));
        accountId = 1L;
    }

    @Test
    public void shouldWithdrawWithPositiveBalance() {
        double amount = 5.0;
        double resultBalance = 1000;
        double limitOverdraft = -10;
        Mockito.when(mockAccount.getLimitOverdraft()).thenReturn(limitOverdraft);
        Mockito.when(mockAccount.removeFromBalance(amount)).thenReturn(resultBalance);

        try {
            checkingAccountService.withdraw(accountId, amount);
        } catch (IllegalValueException | NotFoundException e) {
            fail();
        }
    }

    @Test
    public void shouldWithdrawWithNegativeBalanceWithouExeedOverdraft() {
        double resultBalanceAfterWithdraw = -9;
        double limitOverdraft = -10;
        Mockito.when(mockAccount.getLimitOverdraft()).thenReturn(limitOverdraft);
        Mockito.when(mockAccount.removeFromBalance(amount)).thenReturn(resultBalanceAfterWithdraw);

        try {
            checkingAccountService.withdraw(accountId, amount);
        } catch (IllegalValueException | NotFoundException e) {
            fail();
        }
    }

    @Test(expected = LimitExeceededException.class)
    public void shouldThrowExeptionWhenWithdrawExeedingOverdraft() throws IllegalValueException, NotFoundException {
        double resultBalanceAfterWithdraw = -16.0;
        double limitOverdraft = -10;
        Mockito.when(mockAccount.getLimitOverdraft()).thenReturn(limitOverdraft);
        Mockito.when(mockAccount.removeFromBalance(amount)).thenReturn(resultBalanceAfterWithdraw);

        checkingAccountService.withdraw(accountId, amount);
    }

    @Test
    public void shouldTransferCash() throws NotFoundException, IllegalValueException {
        Long origin = 1L;
        Long destination = 2L;
        double originAccountBalance = 2000;
        Mockito.when(checkingAccountService.withdraw(origin, amount)).thenReturn(originAccountBalance);

        double result = checkingAccountService.transfer(origin, destination, amount);

        assertEquals(originAccountBalance, result, 0);
        Mockito.verify(checkingAccountService, Mockito.never()).deposit(origin, amount);
    }

    @Test
    public void shouldDoRollbackWhenDepositThrowException() throws NotFoundException, IllegalValueException {
        Long origin = 1L;
        Long destination = 2L;
        double originAccountBalance = 2000;
        Mockito.when(checkingAccountService.withdraw(origin, amount)).thenReturn(originAccountBalance);
        Mockito.doThrow(IllegalValueException.class).when(checkingAccountService).deposit(destination, amount);

        checkingAccountService.transfer(origin, destination, amount);

        Mockito.verify(checkingAccountService, Mockito.times(1)).deposit(origin, amount);
    }

    private class MockCheckingAccountService extends CheckingAccountService {

        public MockCheckingAccountService(AccountRepository repository, CheckingAccount account)
                throws NotFoundException {
            this.repository = repository;
            Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(account);
        }
    }

}


