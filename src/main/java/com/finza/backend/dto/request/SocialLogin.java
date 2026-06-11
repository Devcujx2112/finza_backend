package com.finza.backend.dto.request;

import com.finza.backend.constant.BaseMessage;
import com.finza.backend.entity.SocialType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialLogin {
    @NotBlank(message = BaseMessage.INVALID_PROVIDER)
    private SocialType provider;    // "GOOGLE", "FACEBOOK", "APPLE"

    @NotBlank(message = BaseMessage.INVALID_ID_TOKEN)
    private String idToken; // google - apple

    private String accessToken; // facebook
}
