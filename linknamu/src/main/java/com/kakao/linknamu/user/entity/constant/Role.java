package com.kakao.linknamu.user.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
	ROLE_USER("USER", "일반 사용자"),
	ROLE_ADMIN("ADMIN", "관리자");

	@Getter
	private final String roleName;

	@Getter
	private final String description;
}
