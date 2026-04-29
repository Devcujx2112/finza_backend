package com.finza.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseParam<T> {
    public T data;
    public int statusCode;
    public String message;
}
