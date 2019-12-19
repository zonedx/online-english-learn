package com.zone.exception;

/**
 * @ClassName BooksAmountNotEnoughException
 * @Author zone
 * @Date 2019/3/13  0:38
 * @Version 1.0
 * @Description
 */
public class BooksAmountNotEnoughException extends RuntimeException {
    public BooksAmountNotEnoughException(String message) {
        super(message);
    }
}
