package com.kakao.linknamu.thirdparty.googledocs.service;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.thirdparty.googledocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.thirdparty.googledocs.dto.RegisterGoogleDocsRequestDto;
import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googledocs.repository.GooglePageJpaRepository;
import com.kakao.linknamu.thirdparty.googledocs.util.GoogleDocsProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.service.WorkspaceReadService;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class GoogleDocsApiCreateService {
	private final GooglePageJpaRepository googlePageJpaRepository;
	private final WorkspaceReadService workspaceReadService;
	private final WorkspaceSaveService workspaceSaveService;
	private final CategoryService categoryService;
	private final GoogleDocsProvider googleDocsProvider;

	private static final String DEFAULT_WORKSPACE_NAME = "Google Docs";

	public void createDocsApi(RegisterGoogleDocsRequestDto dto, User user) {

		String pageName = googleDocsProvider.getGoogleDocsTitle(dto.documentId());

		// 구글 링크 연동 페이지 존재 유무 검사
		if (googlePageJpaRepository.existsByDocumentIdAndUser(dto.documentId(), user)) {
			throw new Exception400(GoogleDocsExceptionStatus.DOCS_ALREADY_EXIST);
		}

		// 워크스페이스 지정
		Workspace docsWorkspace = workspaceReadService.findWorkspaceByUserAndProvider(user, LinkProvider.GOOGLE_DOCS)
			.orElseGet(() -> workspaceSaveService.createDocsWorkspace(DEFAULT_WORKSPACE_NAME, user));

		// 카테고리 지정, 초기 카테고리의 이름은 pageName 으로 지정한다.
		Category docsCategory = categoryService.findByWorkspaceIdAndCategoryName(docsWorkspace.getId(), pageName)
			.orElseGet(() -> categoryService.save(pageName, docsWorkspace));

		GooglePage googlePage = GooglePage.builder()
			.user(user)
			.category(docsCategory)
			.documentId(dto.documentId())
			.isActive(true)
			.pageName(pageName)
			.build();
		googlePageJpaRepository.save(googlePage);
	}
}
