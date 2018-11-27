package com.sunhill.service;

import com.sunhill.entity.SavingAccount;
import com.sunhill.exception.IllegalValueException;
import com.sunhill.exception.NotFoundException;
import com.sunhill.repository.AccountRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SavingAccountService extends AccountService<SavingAccount> {

    public SavingAccountService() {
        super(new AccountRepository<>(), SavingAccount.class);
    }

    public synchronized double calculateInterest(long accountId) throws IllegalValueException, NotFoundException {
        SavingAccount account = repository.findById(accountId);

        double payment = account.getBalance() * account.getInterestRate();
        if (payment < 0) throw new IllegalValueException();
        return payment;
    }


    public synchronized double payInterest(long accountId) throws IllegalValueException, NotFoundException {
        double interest = calculateInterest(accountId);
        return deposit(accountId, interest);
    }


}
