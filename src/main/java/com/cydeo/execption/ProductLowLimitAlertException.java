package com.cydeo.execption;

public class ProductLowLimitAlertException extends RuntimeException{
    public ProductLowLimitAlertException(String message) {
        super(message);
    }
}
