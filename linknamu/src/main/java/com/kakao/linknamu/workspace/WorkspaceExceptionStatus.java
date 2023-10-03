package com.kakao.linknamu.workspace;

import com.kakao.linknamu._core.exception.BaseExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkspaceExceptionStatus implements BaseExceptionStatus {
    WORKSPACE_DUPLICATED("같은 이름의 워크스페이스가 존재합니다.", 400);

    private final String message;
    private final int status;
}
