package com.cydeo.execption;

public class InvoiceProductNotFoundException extends RuntimeException{
    public InvoiceProductNotFoundException(String message) {
        super(message);
    }
}
