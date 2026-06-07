package com.finza.backend.dto.request;

import com.finza.backend.entity.SocialType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocialLogin {
    @NotBlank
    private SocialType provider;    // "GOOGLE", "FACEBOOK", "APPLE"

    @NotBlank
    private String idToken; // google - apple

    private String accessToken; // facebook
}
