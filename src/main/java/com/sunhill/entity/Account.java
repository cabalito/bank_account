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


    public Account(long owner, double balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public synchronized double addBalance(double amount) {
        balance += amount;
        return balance;
    }

    public synchronized double removeFromBalance(double amount) {
        balance -= amount;
        return balance;
    }

}
