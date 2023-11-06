package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.dto.WorkspaceGetResponseDto;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceReadService {
	private final WorkspaceJpaRepository workspaceJpaRepository;

	public List<WorkspaceGetResponseDto> getWorkspaceList(User user) {
		List<Workspace> workspaceList = workspaceJpaRepository.findAllByUserId(user.getUserId());

		if (workspaceList.isEmpty()) {
			return List.of();
		}

		return workspaceList.stream()
			.map(WorkspaceGetResponseDto::of)
			.toList();
	}

	public Optional<Workspace> findWorkspaceByUserAndName(String workspaceName, User user) {
		return workspaceJpaRepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName);
	}

	public Optional<Workspace> findWorkspaceByUserAndProvider(User user, LinkProvider linkProvider) {
		return workspaceJpaRepository.findByUserIdAndProvider(user.getUserId(), linkProvider);
	}

}
