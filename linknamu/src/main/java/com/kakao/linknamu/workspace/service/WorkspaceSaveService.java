package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceSaveService {
	private final WorkspaceJpaRepository workspaceJpaRepository;

	public Workspace createWorkspace(String workspaceName, User user) {

		workspaceJpaRepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName).ifPresent(
			(w) -> {
				throw new Exception400(WorkspaceExceptionStatus.WORKSPACE_DUPLICATED);
			}
		);

		Workspace workspace = Workspace.builder()
			.workspaceName(workspaceName)
			.user(user)
			.linkProvider(LinkProvider.NORMAL)
			.build();

		return workspaceJpaRepository.save(workspace);
	}

	/*
		노션 연동 전용 워크스페이스 생성.
		만약, "Notion 연동"이라는 워크스페이스가 이미 존재할 경우 해당 워크스페이스를 노션 연동 전용 워크스페이스로 업데이트.
	 */
	public Workspace createNotionWorkspace(String workspaceName, User user) {
		Workspace workspace = workspaceJpaRepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName)
			.orElseGet(() -> Workspace.builder()
				.workspaceName(workspaceName)
				.user(user)
				.build());
		workspace.setLinkProvider(LinkProvider.NOTION);
		return workspaceJpaRepository.saveAndFlush(workspace);
	}

	public Workspace createDocsWorkspace(String workspaceName, User user) {
		Workspace workspace = workspaceJpaRepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName)
			.orElseGet(() -> Workspace.builder()
				.workspaceName(workspaceName)
				.user(user)
				.build());
		workspace.setLinkProvider(LinkProvider.GOOGLE_DOCS);
		return workspaceJpaRepository.saveAndFlush(workspace);
	}
}
