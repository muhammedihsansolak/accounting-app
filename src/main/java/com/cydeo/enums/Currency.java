package com.cydeo.enums;

import lombok.Getter;

@Getter
public enum Currency {
    EUR("eur"), USD("usd");

    private final String currency;

    Currency(String currency) {
        this.currency = currency;
    }
}