package com.cydeo.exception;

public class NoEnoughStockException extends RuntimeException{
    public NoEnoughStockException(String message) {
        super(message);
    }
}