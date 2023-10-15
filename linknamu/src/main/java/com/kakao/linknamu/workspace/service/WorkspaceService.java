package com.kakao.linknamu.workspace.service;

import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceJPARepository workspaceJPARepository;

    public Workspace getWorkspaceById(Long id) {
        Optional<Workspace> workspaceOP = workspaceJPARepository.findById(id);
        Workspace workspace = workspaceOP.orElseThrow(
                () -> new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND)
        );


        return workspace;
    }


}
