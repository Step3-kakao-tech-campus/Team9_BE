package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.dto.WorkspaceGetResponseDto;
import com.kakao.linknamu.workspace.dto.WorkspaceUpdateRequestDto;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceService {
	private final WorkspaceJpaRepository workspaceJpaRepository;

	public Workspace getWorkspaceById(Long workspaceId) {
		return workspaceJpaRepository.findById(workspaceId).orElseThrow(
			() -> new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND)
		);
	}

	public Workspace createWorkspace(String workspaceName, User user) {
		validDuplicatedWorkspaceName(workspaceName, user);

		Workspace workspace = Workspace.builder()
			.workspaceName(workspaceName)
			.user(user)
			.linkProvider(LinkProvider.NORMAL)
			.build();

		return workspaceJpaRepository.save(workspace);
	}

	public List<WorkspaceGetResponseDto> getWorkspaceList(User user) {
		List<Workspace> workspaceList = workspaceJpaRepository.findAllByUserId(user.getUserId());

		if (workspaceList.isEmpty()) {
			return List.of();
		}

		return workspaceList.stream()
			.map(WorkspaceGetResponseDto::of)
			.toList();
	}

	public Workspace findWorkspaceByUserAndName(String workspaceName, User user) {
		return workspaceJpaRepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName).orElseThrow(
				() -> new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND)
			);
	}

	public Workspace findWorkspaceByUserAndProvider(String workspaceName, User user, LinkProvider linkProvider) {
		return workspaceJpaRepository.findByUserIdAndProvider(user.getUserId(), linkProvider).orElseGet(
			() -> createWorkspaceWithLinkProvider(workspaceName, user, linkProvider)
		);
	}

	public void updateWorkspaceName(Long workspaceId, WorkspaceUpdateRequestDto requestDto, User user) {
		Workspace workspace = getWorkspaceById(workspaceId);

		validUser(workspace, user);

		// 바꾸고자 하는 워크스페이스 이름이 중복 시 예외를 발생시킨다.
		validDuplicatedWorkspaceName(requestDto.workspaceName(), user);

		// 만약 수정하고자하는 이름이 같다면 DB에 update할 이유가 없다.
		if (requestDto.workspaceName().equals(workspace.getWorkspaceName())) {
			return;
		}

		workspace.renameWorkspace(requestDto.workspaceName());
	}

	public void deleteWorkspace(Long workspaceId, User user) {
		Workspace workspace = getWorkspaceById(workspaceId);
		validUser(workspace, user);
		workspaceJpaRepository.delete(workspace);
	}



	// 노션 / 구글독스 연동 전용 워크스페이스 생성.
	private Workspace createWorkspaceWithLinkProvider(String workspaceName, User user, LinkProvider linkProvider) {
		Workspace workspace = workspaceJpaRepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName)
			.orElseGet(() -> Workspace.builder()
				.workspaceName(workspaceName)
				.user(user)
				.build());
		workspace.setLinkProvider(linkProvider);
		return workspaceJpaRepository.saveAndFlush(workspace);
	}

	private void validUser(Workspace workspace, User user) {
		if (!workspace.getUser().getUserId().equals(user.getUserId())) {
			throw new Exception403(WorkspaceExceptionStatus.WORKSPACE_FORBIDDEN);
		}
	}

	private void validDuplicatedWorkspaceName(String workspaceName, User user) {
		workspaceJpaRepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName)
			.ifPresent((w) -> {
				throw new Exception400(WorkspaceExceptionStatus.WORKSPACE_DUPLICATED);
			});
	}

}
