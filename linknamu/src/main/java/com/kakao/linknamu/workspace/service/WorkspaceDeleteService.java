package com.kakao.linknamu.workspace.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceDeleteService {
	private final WorkspaceJPARepository workspaceJPARepository;

	public void deleteWorkspace(Long workspaceId, User user) {
		Workspace workspace = workspaceJPARepository.findById(workspaceId).orElseThrow(
			() -> new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND));

		validationCheck(workspace.getUser().getUserId(), user.getUserId());

		workspaceJPARepository.delete(workspace);
	}

	private void validationCheck(Long writerId, Long requesterId) {
		if (!writerId.equals(requesterId))
			throw new Exception403(WorkspaceExceptionStatus.WORKSPACE_FORBIDDEN);
	}
}
