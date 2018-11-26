package com.sunhill.exception;


public class LimitExeceededException extends IllegalValueException {

    public LimitExeceededException(){
        super("Operation failed: Account limit exeeded");
    }
}
