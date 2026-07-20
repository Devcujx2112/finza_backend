package com.finza.backend.service.impl;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.constant.StatusCode;
import com.finza.backend.exception.AppException;
import com.finza.backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendOtp(String email, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject("Finza - Xác nhận đặt lại mật khẩu");
            helper.setText(buildOtpEmailTemplate(otp), true);  // true = HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new AppException(StatusCode.INTERNAL_SERVER_ERROR, BaseMessage.sendOtpFail);
        }
    }

    private String buildOtpEmailTemplate(String otp) {
        String template = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                    </head>
                    <body style="margin:0; padding:0; background-color:#f5f5f5; font-family: Arial, sans-serif;">
                        <table width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="center" style="padding: 40px 0;">
                                    <table width="480" cellpadding="0" cellspacing="0"
                                           style="background:#ffffff; border-radius:12px; overflow:hidden;
                                                  box-shadow: 0 2px 8px rgba(0,0,0,0.08);">
                                        <!-- Header -->
                                        <tr>
                                            <td align="center"
                            style="background: #35C55F;
                                                                                       padding: 32px;">
                                                <h1 style="color:#ffffff; margin:0; font-size:28px; text-shadow: 0 1px 3px rgba(0,0,0,0.2);">Finza</h1>
                                                <p style="color:rgba(255,255,255,0.9); margin:8px 0 0;">
                                                    Quản lý tài chính thông minh
                                                </p>
                                            </td>
                                        </tr>
                                        <!-- Body -->
                                        <tr>
                                            <td style="padding: 32px;">
                                                <p style="color:#333; font-size:16px; margin:0 0 8px;">
                                                    Xin chào,
                                                </p>
                                                <p style="color:#555; font-size:15px; line-height:1.6;">
                                                    Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.
                                                    Sử dụng mã OTP bên dưới để tiếp tục:
                                                </p>
                                                <!-- OTP Box -->
                                                <div style="text-align:center; margin: 28px 0;">
                                                    <div style="display:inline-block;
                                                                background:#f0fff4;
                                                                border: 2px dashed #35C55F;
                                                                border-radius:12px;
                                                                padding: 20px 48px;">
                                                        <p style="margin:0; font-size:13px; color:#888;">Mã OTP của bạn</p>
                                                        <p style="margin:8px 0 0; font-size:36px; font-weight:bold;
                                                                  letter-spacing:8px; color:#35C55F;">
                                                            OTP_VALUE
                                                        </p>
                                                    </div>
                                                </div>
                                                <p style="color:#e74c3c; font-size:14px; text-align:center;">
                                                    ⏰ Mã có hiệu lực trong <strong>3 phút</strong>
                                                </p>
                                                <hr style="border:none; border-top:1px solid #eee; margin: 24px 0;">
                                                <p style="color:#999; font-size:13px; text-align:center; margin:0;">
                                                    Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này.<br>
                                                    Tài khoản của bạn vẫn an toàn.
                                                </p>
                                            </td>
                                        </tr>
                                        <!-- Footer -->
                                        <tr>
                                            <td align="center"
                                                style="background:#f8f8f8; padding:16px;
                                                       border-top:1px solid #eee;">
                                                <p style="color:#bbb; font-size:12px; margin:0;">
                                                    © 2024 Finza. All rights reserved.
                                                </p>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </body>
                    </html>
                """;

        return template.replace("OTP_VALUE", otp);
    }
}
