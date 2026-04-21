package com.finza.backend.dto.response;

import lombok.Data;

@Data
public class BaseResponse<T>{
    private int statusCode;
    private String message;
    private T data;

    // Khi có data
    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setStatusCode(200);
        response.setMessage("Success");
        response.setData(data);
        return response;
    }

    // Khi không có data
    public static <T> BaseResponse<T> success(String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setStatusCode(200);
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    // Khi có lỗi
    public static <T> BaseResponse<T> error(int statusCode, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        response.setData(null);
        return response;
    }
}
