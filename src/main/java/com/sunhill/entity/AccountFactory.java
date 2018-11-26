package com.sunhill.entity;

import com.sunhill.exception.UnsuportedAccountTypeException;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountFactory {

    private final long owner;
    private final double balance;

    //CheckingAccount
    private final double limitOverdraft;

    //SavingAccount
    private final double interestRate;

    private AccountFactory(Builder builder){
        this.owner = builder.owner;
        this.balance = builder.balance;

        this.limitOverdraft = builder.limitOverdraft;

        this.interestRate = builder.interestRate;
    }

    public Account createEntity(Class accountClass) throws UnsuportedAccountTypeException {
        if(accountClass.equals(CheckingAccount.class)){
            return new CheckingAccount(this.owner, this.balance, this.limitOverdraft);
        }else if(accountClass.equals(SavingAccount.class)){
            return new SavingAccount(this.owner, this.balance, this.interestRate);
        }
        throw new UnsuportedAccountTypeException();

    }

    public static class Builder{
        private long owner;
        private double balance;

        //CheckingAccount
        private double limitOverdraft;

        //SavingAccount
        private double interestRate;

        public Builder(long owner, double balance){
            this.owner = owner;
            this.balance = balance;
        }

        public Builder limitOverdraft(double limitOverdraft){
            this.limitOverdraft = limitOverdraft;
            return this;
        }

        public Builder interestRate(double interestRate){
            this.interestRate = interestRate;
            return this;
        }

        public AccountFactory build(){
            return new AccountFactory(this);
        }

    }


}
