package com.kakao.linknamu.user.dto;

import lombok.Builder;

public record LoginResponseDto(
        String accessToken,
        String refreshToken
) {
    @Builder
    public LoginResponseDto {
    }

    public static LoginResponseDto of(String accessToken, String refreshToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
