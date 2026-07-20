package com.finza.backend.constant;

public class BaseMessage {
    // Account
    public static final String EMAIL_EXISTED = "Email đã tồn tại";
    public static final String PHONE_EXISTED = "Số điện thoại này đã được đăng kí trên tài khoản khác";
    public static final String ACCOUNT_NOT_FOUND = "Không tìm thấy tài khoản";
    public static final String WRONG_PASSWORD = "Sai mật khẩu";
    public static final String REGISTER_SUCCESS = "Đăng ký thành công";
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String GET_PROFILE_SUCCESS = "Lấy thông tin thành công";
    public static final String NOT_NULL_EMAIL = "Email không được để trống";
    public static final String NOT_NULL_FULLNAME = "Họ tên không được để trống";
    public static final String NOT_NULL_PHONENUMBER = "Số điện thoại không được để trống";
    public static final String NOT_NULL_PASSWORD = "Mật khẩu không được để trống";
    public static final String LENGHT_PASWORD = "Mật khẩu phải có ít nhất 6 ký tự";
    public static final String NOT_NULL_ACCESSTOKEN = "Refresh token không được để trống";
    public static final String NOT_VALID_TOKEN = "Refresh token không hợp lệ";
    public static final String TOKEN_RECALL = "Refresh token đã bị thu hồi";
    public static final String TOKEN_EXPIRED = "Refresh token đã hết hạn, vui lòng đăng nhập lại";
    public static final String SOCIAL_ACCOUNT_NO_PASSWORD =
            "Email này đã đăng ký bằng Google, vui lòng đăng nhập bằng Google";
    public static final String INVALID_ID_TOKEN = "Id provider không được để trống";
    public static final String UNSUPPORTED_PROVIDER = "Không hỗ trợ cách đăng nhập này";
    public static final String LOGIN_SOCIAL_SUCCESS = "Đăng nhập bằng social thành công";
    public static final String INVALID_PROVIDER = "Provider không được để trống";
    public static final String SUCCESS = "Thành công";
    public static final String EmailAndPhoneNumberNull = "Email hoặc số điện thoại không được để trống";
    public static final String EmailIsNotEXISTED = "Email không tồn tại";
    public static final String SendOtpSuccess = "Gửi otp thành công";
}