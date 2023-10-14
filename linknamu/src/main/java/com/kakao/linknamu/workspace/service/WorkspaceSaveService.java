package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
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

    public Workspace createWorkspace(String workspaceName, User user) {
        workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName).ifPresent(
                (w) -> {
                    throw new Exception400(WorkspaceExceptionStatus.WORKSPACE_DUPLICATED);
                }
        );

        Workspace workspace = Workspace.builder()
                .workspaceName(workspaceName)
                .user(user)
                .linkProvider(LinkProvider.NORMAL)
                .build();

        return workspaceJPARepository.save(workspace);
    }

    /*
        노션 연동 전용 워크스페이스 생성.
        만약, "Notion 연동"이라는 워크스페이스가 이미 존재할 경우 해당 워크스페이스를 노션 연동 전용 워크스페이스로 업데이트.
     */
    public Workspace createNotionWorkspace(String workspaceName, User user) {
        Workspace workspace = workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName)
                .orElseGet(() -> Workspace.builder()
                        .workspaceName(workspaceName)
                        .user(user)
                        .build());
        workspace.setLinkProvider(LinkProvider.NOTION);
        return workspaceJPARepository.saveAndFlush(workspace);
    }

    // createNotionWorkspace 메서드와 코드 중복이 심하다. 추후 리펙토링 필요.
    public Workspace createDocsWorkspace(String workspaceName, User user) {
        Workspace workspace = workspaceJPARepository.findByUserIdAndWorkspaceName(user.getUserId(), workspaceName)
                .orElseGet(() -> Workspace.builder()
                        .workspaceName(workspaceName)
                        .user(user)
                        .build());
        workspace.setLinkProvider(LinkProvider.GOOGLE_DOCS);
        return workspaceJPARepository.saveAndFlush(workspace);
    }
}
