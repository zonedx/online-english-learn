package com.zone.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName NewWord
 * @Author zone
 * @Date 2018/12/31  14:35
 * @Version 1.0
 * @Description
 */
public class NewWord implements Serializable {
    private String type;
    private int errorCode;
    private int elapsedTime;
    private List<List<ThirdWord>> translateResult;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public List<List<ThirdWord>> getTranslateResult() {
        return translateResult;
    }

    public void setTranslateResult(List<List<ThirdWord>> translateResult) {
        this.translateResult = translateResult;
    }

    @Override
    public String toString() {
        return "Word{" +
                "type='" + type + '\'' +
                ", errorCode=" + errorCode +
                ", elapsedTime=" + elapsedTime +
                ", translateResult=" + translateResult +
                '}';
    }
}
