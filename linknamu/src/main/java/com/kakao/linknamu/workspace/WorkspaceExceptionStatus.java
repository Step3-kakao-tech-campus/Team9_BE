package com.kakao.linknamu.workspace;

import com.kakao.linknamu.core.exception.BaseExceptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkspaceExceptionStatus implements BaseExceptionStatus {
	WORKSPACE_DUPLICATED("같은 이름의 워크스페이스가 존재합니다.", 400, 64000),
	WORKSPACE_FORBIDDEN("접근 권한이 없는 사용자 입니다.", 403, 64030),
	WORKSPACE_NOT_FOUND("존재하지 않는 워크스페이스 입니다.", 404, 64040);

	private final String message;
	private final int status;
	private final int errorCode;
}
