package com.sunhill.service;

import com.sunhill.entity.CheckingAccount;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.repository.AccountRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CheckingAccountService extends AccountService<CheckingAccount> {

    public CheckingAccountService(){
        super(new AccountRepository<>(), CheckingAccount.class);
    }

    @Override
    public double withdraw(long accountId, double amount) throws IllegalValueException, NotFoundException {
        if(amount<0) throw new IllegalValueException();
        CheckingAccount account = repository.findById(accountId);

        return withdraw(account, amount, account.getLimitOverdraft());
    }



}
