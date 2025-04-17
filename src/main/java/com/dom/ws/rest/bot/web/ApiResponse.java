package com.dom.ws.rest.bot.web;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int code;

    public ApiResponse() {
        this.success = true;
        this.code = 0;
    }

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.code = 0;
    }

    public ApiResponse(boolean success, String message, int code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public ApiResponse(boolean success, String message, T data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        return new ApiResponse<>(false, message, code);
    }

    public static <T> ApiResponse<T> error(String message, T data, int code) {
        return new ApiResponse<>(false, message, data, code);
    }
}