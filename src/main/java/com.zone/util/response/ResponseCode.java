package com.zone.util.response;

public enum ResponseCode {
    SUCCESS(200),
    SUCCESS_NO_POINTS(201),
    FAIL(400),
    NOT_FOUND(404),//接口不存在

    NOT_LOGIN_NO_ACCESS(444), //未登录，不能访问
    USERNAME_ERROR_OR_PASSWORD_ERROR(445), //用户名或者密码错误

    INTERNAL_SERVER_ERROR(500);//服务器内部错误
    public int code;

    ResponseCode(int code) {
        this.code=code;
    }
}
