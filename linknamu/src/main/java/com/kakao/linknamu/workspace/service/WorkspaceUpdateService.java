package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.dto.WorkspaceUpdateRequestDto;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.repository.WorkspaceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceUpdateService {
	private final WorkspaceJpaRepository workspaceJpaRepository;

	public void updateWorkspaceName(Long workspaceId, WorkspaceUpdateRequestDto requestDto, User user) {
		Workspace workspace = workspaceJpaRepository.findById(workspaceId).orElseThrow(
			() -> new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND));

		validationCheck(workspace.getUser().getUserId(), user.getUserId());

		// 만약 수정하고자하는 이름이 같다면 DB에 update할 이유가 없다.
		if (requestDto.workspaceName().equals(workspace.getWorkspaceName())) {
			return;
		}

		workspace.renameWorkspace(requestDto.workspaceName());
	}

	private void validationCheck(Long writerId, Long requesterId) {
		if (!writerId.equals(requesterId)) {
			throw new Exception403(WorkspaceExceptionStatus.WORKSPACE_FORBIDDEN);
		}
	}
}
