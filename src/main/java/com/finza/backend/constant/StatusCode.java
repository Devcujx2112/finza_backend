package com.finza.backend.constant;

public class StatusCode {
    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SIZE_PASSWORD = 6;
    public static final int EmailAlreadyExists = 409;
    public static final int EmailNotExists = 1000;
    public static final int PasswordNotNull = 1001;
    public static final int PasswordNotCorrect = 1002;
    public static final int UNSUPPORTED_PROVIDER = 1003;
    public static final int TOKEN_EXPIRED = 1004;
    public static final int PhoneAlreadyExists = 410;
    public static final int EmailAndPhoneNumberNull = 1004;
    public static final int EmailIsRegisterInSocial = 1005;
    public static final int OTP_EXPIRED = 1006;
    public static final int OTP_INVALID = 1007;
    public static final int OTP_NULL = 1008;
}
