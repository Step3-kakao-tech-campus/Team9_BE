package com.kakao.linknamu.user.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakao.linknamu.user.entity.constant.Provider;

public record GoogleUserInfo(
        String id,
        String email,
        @JsonProperty("verified_email") Boolean verifiedEmail,
        String picture
)implements OauthUserInfo {
    @Override
    public Provider provider() {
        return Provider.PROVIDER_GOOGLE;
    }
}
