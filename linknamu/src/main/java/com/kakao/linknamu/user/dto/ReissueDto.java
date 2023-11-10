package com.kakao.linknamu.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class ReissueDto {


	public record ReissueRequestDto(
		@NotNull(message = "Refresh 토큰을 입력해주세요.")
		String refreshToken
	) {
		@Builder
		public ReissueRequestDto {
		}
	}

	public record ReissueResponseDto(
		String accessToken,
		String refreshToken
	) {
		@Builder
		public ReissueResponseDto {
		}

		public static ReissueResponseDto of(String accessToken, String refreshToken) {
			return ReissueResponseDto.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
		}
	}
}
