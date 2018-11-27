package com.sunhill.exception;


public class UnsuportedAccountTypeException extends Exception {

    public UnsuportedAccountTypeException() {
        super("Account class to instance not found");
    }
}
