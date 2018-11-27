package com.sunhill.unit.repository;

import com.sunhill.entity.Account;
import com.sunhill.exception.NotFoundException;
import com.sunhill.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;


public class AccountRepositoryTest {

    private AccountRepository<Account> accountRepository;
    private Long accountId;
    private Long fake;
    private Account mockAccount;

    @Before
    public void setUp() {
        accountRepository = new AccountRepository<>();
        mockAccount = Mockito.mock(Account.class);
        accountId = accountRepository.insert(mockAccount);
        fake = -3L;
    }

    @Test
    public void shouldFindAccountById() throws NotFoundException {
        Account result = accountRepository.findById(accountId);

        assertEquals(mockAccount, result);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionIfAccountDoesNotExists() throws NotFoundException {
        accountRepository.findById(fake);
    }
}