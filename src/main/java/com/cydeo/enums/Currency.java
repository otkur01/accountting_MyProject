package com.cydeo.enums;

import lombok.Getter;

@Getter
public enum Currency {

    EUR("Eur"), USD("Usd");

    private final String value;

    Currency(String value) {
        this.value = value;
    }
}
