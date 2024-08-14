package com.cydeo.execption;

public class ClientVendorNotFoundException extends RuntimeException{
    public ClientVendorNotFoundException(String message) {
        super(message);
    }
}
