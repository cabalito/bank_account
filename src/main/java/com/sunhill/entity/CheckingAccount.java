package com.sunhill.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CheckingAccount extends Account {

    protected double limitOverdraft;

    public CheckingAccount(long owner, double balance, double limitOverdraft){
        super(owner,balance);
        this.limitOverdraft = limitOverdraft;
    }
}
