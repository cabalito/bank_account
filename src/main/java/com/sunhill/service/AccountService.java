package com.sunhill.service;

import com.sunhill.entity.Account;
import com.sunhill.entity.AccountFactory;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.LimitExeceededException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.exception.UnsuportedAccountTypeException;
import com.sunhill.repository.AccountRepository;


public abstract class AccountService<A extends Account> {
    //TODO: Change by springboot service

    private final Class<A> accountType;

    protected AccountRepository<A> repository;

    public AccountService(AccountRepository<A> repository, Class<A> accountClass) {
        this.accountType = accountClass;
        this.repository = repository;
    }


    public long createAccount(AccountFactory accountFactory)
            throws IllegalValueException, UnsuportedAccountTypeException {
        if (accountFactory == null
                || accountFactory.getBalance() < 0) throw new IllegalValueException();
        @SuppressWarnings("unchecked")
        A account = (A) accountFactory.createEntity(accountType);

        return repository.insert(account);
    }

    public synchronized double deposit(long accountId, double increment)
            throws IllegalValueException, NotFoundException {

        if (increment < 0) throw new IllegalValueException();
        Account account = repository.findById(accountId);

        return account.addBalance(increment);
    }

    public synchronized double withdraw(long accountId, double amount) throws IllegalValueException, NotFoundException {
        if (amount < 0) throw new IllegalValueException();
        Account account = repository.findById(accountId);

        double DEFAULT_LIMIT_OVERDRAFT = 0;
        return withdraw(account, amount, DEFAULT_LIMIT_OVERDRAFT);
    }

    protected synchronized double withdraw(Account account, double amount, double allowedOverdraft) throws LimitExeceededException, NotFoundException {

        double result = account.removeFromBalance(amount);
        if (result < allowedOverdraft) {
            account.addBalance(amount);
            throw new LimitExeceededException();
        }
        return result;
    }


    public double getBalance(Long accountId) throws NotFoundException {
        Account account = repository.findById(accountId);
        return account.getBalance();
    }
}
