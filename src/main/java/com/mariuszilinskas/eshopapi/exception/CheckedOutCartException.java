package com.mariuszilinskas.eshopapi.exception;

public class CheckedOutCartException extends RuntimeException {

    public CheckedOutCartException() {
        super("This Cart is checked out. No more changes can be made.");
    }
}

