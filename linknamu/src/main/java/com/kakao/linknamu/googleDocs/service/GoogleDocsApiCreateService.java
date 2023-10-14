package com.kakao.linknamu.googleDocs.service;

import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.googleDocs.dto.GoogleDocsApiRegistrationRequestDto;
import com.kakao.linknamu.googleDocs.repository.GooglePageJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.service.WorkspaceReadService;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class GoogleDocsApiCreateService {
    private final GooglePageJPARepository googlePageJPARepository;
    private final WorkspaceReadService workspaceReadService;
    private final WorkspaceSaveService workspaceSaveService;
    private final CategoryService categoryService;

    private final static String DEFAULT_WORKSPACE_NAME = "Google Docs";

    public void createDocsApi(GoogleDocsApiRegistrationRequestDto dto, User user) {

    }
}
