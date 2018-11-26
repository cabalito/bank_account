package com.sunhill.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Account {

    protected long id;
    protected long owner;
    protected double balance;


    public Account(long owner, double balance){
        this.owner = owner;
        this.balance = balance;
    }

    public double addBalance(double amount){
        return getBalance() + amount;
    }

    public double removeFromBalance(double amount){
        return getBalance() - amount;
    }

}
