package com.wisdom.auth.provider.pojo;

public class ResponseData<T> {
    public static final int SUCCESS = 200;
    public static final int ERROR = 300;
    private Integer statusCode;
    private String message;
    private T resultData;

    public ResponseData() {
    }

    public ResponseData(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.resultData = null;
    }

    public ResponseData(Integer statusCode, T resultData) {
        this.statusCode = statusCode;
        this.message = null;
        this.resultData = resultData;
    }

    public ResponseData(Integer statusCode, String message, T resultData) {
        this.statusCode = statusCode;
        this.message = message;
        this.resultData = resultData;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResultData() {
        return this.resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }
}
