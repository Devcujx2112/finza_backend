package com.finza.backend.dto.response;

import lombok.Data;

@Data
public class BaseResponse<T>{
    private int statusCode;
    private String message;
    private T data;

    // Khi có data
    public static <T> BaseResponse<T> success(BaseParam<T> param) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setStatusCode(param.statusCode);
        response.setMessage(param.message);
        response.setData(param.data);
        return response;
    }

    // Khi không có data
    public static <T> BaseResponse<T> successNullData(BaseParam<T> param) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setStatusCode(param.statusCode);
        response.setMessage(param.message);
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
