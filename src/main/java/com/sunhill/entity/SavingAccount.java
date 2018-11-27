package com.sunhill.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SavingAccount extends Account {

    protected double interestRate;

    public SavingAccount(long owner, double balance, double interestRate) {
        super(owner, balance);
        this.interestRate = interestRate;
    }

}
