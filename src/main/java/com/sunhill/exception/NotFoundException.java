package com.sunhill.exception;


public class NotFoundException extends Exception {

    public NotFoundException() {
        super("Element does not exists");
    }
}
