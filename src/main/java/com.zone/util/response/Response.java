package com.zone.util.response;

import java.io.Serializable;

/**
 * @ClassName Requesrt
 * @Author zone
 * @Date 2018/12/27  14:52
 * @Version 1.0
 * @Description 对响应结果进行封装
 */
public class Response implements Serializable {
    private int code;
    private String messeage;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMesseage() {
        return messeage;
    }

    public void setMesseage(String messeage) {
        this.messeage = messeage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Response() {
    }

    public Response setCode(ResponseCode responseCode){
        this.code=responseCode.code;
        return this;
    }
    public Response(int code, String messeage, Object data) {
        this.code = code;
        this.messeage = messeage;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", messeage='" + messeage + '\'' +
                ", data=" + data +
                '}';
    }
}
