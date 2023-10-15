package com.kakao.linknamu.googleDocs.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.googleDocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.googleDocs.dto.GoogleDocsApiRegistrationRequestDto;
import com.kakao.linknamu.googleDocs.entity.GooglePage;
import com.kakao.linknamu.googleDocs.repository.GooglePageJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
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
        // 구글 링크 연동 페이지 존재 유무 검사
        if(googlePageJPARepository.existsByDocumentIdAndUser(dto.documentId(), user)) {
            throw new Exception400(GoogleDocsExceptionStatus.DOCS_ALREADY_EXIST);
        }

        // 워크스페이스 지정
        Workspace docsWorkspace = workspaceReadService.findWorkspaceByUserAndProvider(user, LinkProvider.GOOGLE_DOCS)
                .orElseGet(() -> workspaceSaveService.createDocsWorkspace(DEFAULT_WORKSPACE_NAME, user));

        // 카테고리 지정, 초기 카테고리의 이름은 pageName 으로 지정한다.
        Category docsCategory = categoryService.findByWorkspaceIdAndCategoryName(docsWorkspace.getId(), dto.pageName())
                .orElseGet(() -> categoryService.save(dto.pageName(), docsWorkspace));

        GooglePage googlePage = GooglePage.builder()
                .user(user)
                .category(docsCategory)
                .documentId(dto.documentId())
                .isActive(true)
                .pageName(dto.pageName())
                .build();
        googlePageJPARepository.save(googlePage);
    }
}
