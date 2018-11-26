package com.sunhill.service;

import com.sunhill.entity.Account;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.repository.AccountRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CheckingAccountService {

    protected AccountRepository repository;

    public CheckingAccountService(){
        this.repository = new AccountRepository();
    }


    public long createAccount(double balance) throws IllegalValueException {

        Account account = new Account(11L, balance);

        return repository.insert(account);
    }

    public synchronized double deposit(long accountId, double increment)
            throws IllegalValueException, NotFoundException {

        if(increment<0) throw new IllegalValueException();
        Account account = repository.findById(accountId);

        return account.addBalance(increment);
    }

    public  synchronized double withdraw(long accountId, double amount) throws IllegalValueException, NotFoundException {
        if(amount<0) throw new IllegalValueException();
        Account account = repository.findById(accountId);

        return account.removeFromBalance(amount) ;
    }



}
