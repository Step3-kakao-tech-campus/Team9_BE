package com.kakao.linknamu.user.dto.oauth;

import com.kakao.linknamu.user.entity.constant.Provider;

public interface OauthUserInfo {
    String email();
    Provider provider();
    String id();
}
