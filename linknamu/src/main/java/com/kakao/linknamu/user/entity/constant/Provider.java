package com.kakao.linknamu.user.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Provider{
    PROVIDER_NORMAL("normal", "일반 회원가입"),
    PROVIDER_GOOGLE("google", "구글");

    @Getter
    private final String provider;

    @Getter
    private final String decription;
}
