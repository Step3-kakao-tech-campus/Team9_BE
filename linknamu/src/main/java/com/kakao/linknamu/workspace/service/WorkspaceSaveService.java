package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceSaveService {
    private final WorkspaceJPARepository workspaceJPARepository;

    public void createWorkspace(String workspaceName, User user) {
        workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName).ifPresent(
                (w) -> {
                    throw new Exception400(WorkspaceExceptionStatus.WORKSPACE_DUPLICATED);
                }
        );

        Workspace workspace = Workspace.builder()
                .workspaceName(workspaceName)
                .user(user)
                .build();

        workspaceJPARepository.save(workspace);
    }
}
