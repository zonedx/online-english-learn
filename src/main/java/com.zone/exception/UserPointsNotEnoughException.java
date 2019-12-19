package com.zone.exception;

/**
 * @ClassName UserPointsNotEnoughException
 * @Author zone
 * @Date 2019/3/13  1:06
 * @Version 1.0
 * @Description
 */
public class UserPointsNotEnoughException extends RuntimeException{
    public UserPointsNotEnoughException(String message) {
        super(message);
    }
}
