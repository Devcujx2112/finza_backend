package com.finza.backend.dto.request;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountRequest {

    @NotBlank(message = BaseMessage.NOT_NULL_USERNAME)
    private String userName;

    @NotBlank(message = BaseMessage.NOT_NULL_FULLNAME)
    private String fullName;

    @NotBlank(message = BaseMessage.NOT_NULL_PHONENUMBER)
    private String phoneNumber;

    @NotBlank(message = BaseMessage.NOT_NULL_PASSWORD)
    @Size(min = StatusCode.SIZE_PASSWORD, message = BaseMessage.LENGHT_PASWORD)
    private String password;

    private String dateOfBirth;
}