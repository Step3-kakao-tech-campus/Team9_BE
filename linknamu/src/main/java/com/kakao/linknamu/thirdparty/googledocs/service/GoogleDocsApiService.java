package com.kakao.linknamu.thirdparty.googledocs.service;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.thirdparty.googledocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.thirdparty.googledocs.dto.RegisterGoogleDocsRequestDto;
import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googledocs.repository.GooglePageJpaRepository;
import com.kakao.linknamu.thirdparty.googledocs.util.GoogleDocsProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class GoogleDocsApiService {
	private final GooglePageJpaRepository googlePageJpaRepository;
	private final CategoryService categoryService;
	private final GoogleDocsProvider googleDocsProvider;
	private final WorkspaceService workspaceService;
	private static final String DEFAULT_WORKSPACE_NAME = "Google Docs";

	public void createDocsApi(RegisterGoogleDocsRequestDto dto, User user) {
		String pageName = googleDocsProvider.getGoogleDocsTitle(dto.documentId());

		// 구글 링크 연동 페이지 존재 유무 검사
		if (googlePageJpaRepository.existsByDocumentIdAndUser(dto.documentId(), user)) {
			throw new Exception400(GoogleDocsExceptionStatus.DOCS_ALREADY_EXIST);
		}

		// 워크스페이스 지정
		Workspace docsWorkspace = workspaceService.findWorkspaceByUserAndProvider(DEFAULT_WORKSPACE_NAME, user,
			LinkProvider.GOOGLE_DOCS);

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

	public void deleteDocsPage(User user, Long docsPageId) {
		GooglePage googlePage = googlePageJpaRepository.findById(docsPageId)
			.orElseThrow(() -> new Exception404(GoogleDocsExceptionStatus.DOCS_NOT_FOUND));
		validUser(googlePage, user);
		googlePageJpaRepository.delete(googlePage);
	}

	private void validUser(GooglePage googlePage, User user) {
		if (!googlePage.getUser().getUserId().equals(user.getUserId())) {
			throw new Exception403(GoogleDocsExceptionStatus.DOCS_FORBIDDEN);
		}
	}
}
