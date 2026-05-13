package com.finza.backend.exception;

import com.finza.backend.constant.StatusCode;
import com.finza.backend.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseResponse<Void>> handleAppException(AppException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(BaseResponse.error(ex.getStatusCode(), ex.getMessage()));
    }
}
