package com.sunhill.exception;


public class IllegalValueException extends Exception {

    public IllegalValueException() {
        this("");
    }

    public IllegalValueException(String s) {
        super("Value not allowed " + s);
    }
}
