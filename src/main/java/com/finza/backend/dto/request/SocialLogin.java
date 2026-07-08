package com.finza.backend.dto.request;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.entity.SocialType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.context.annotation.Primary;

@Data
public class SocialLogin {
    @Enumerated(EnumType.STRING)
    private SocialType provider;    // "GOOGLE", "FACEBOOK", "APPLE"

    private String idToken; // google - apple

    private String accessToken; // facebook

    private String fullName;
}
