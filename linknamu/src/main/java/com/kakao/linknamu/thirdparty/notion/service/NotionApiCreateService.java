package com.kakao.linknamu.thirdparty.notion.service;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.thirdparty.notion.NotionExceptionStatus;
import com.kakao.linknamu.thirdparty.notion.dto.RegisterNotionRequestDto;
import com.kakao.linknamu.thirdparty.notion.entity.NotionAccount;
import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;
import com.kakao.linknamu.thirdparty.notion.repository.NotionAccountJpaRepository;
import com.kakao.linknamu.thirdparty.notion.repository.NotionPageJpaRepository;
import com.kakao.linknamu.thirdparty.notion.util.NotionProvider;
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
public class NotionApiCreateService {
	private final NotionAccountJpaRepository notionAccountJpaRepository;
	private final NotionPageJpaRepository notionPageJpaRepository;
	private final WorkspaceReadService workspaceReadService;
	private final WorkspaceSaveService workspaceSaveService;
	private final CategoryService categoryService;
	private final NotionProvider notionProvider;

	private static final String DEFAULT_WORKSPACE_NAME = "Notion 연동";

	/*
		로직
		1. 입력 accessToken이 유효한지 판단
		2. noticeAccount가 없다면 새로 생성
		3. 기존에 등록한 적이 있는지 확인 -> 있다면 예외처리
		4. 링크들을 연동할 워크스페이스 및 카테고리 생성
		5. noticePage 생성
	 */
	public void createNotionApi(String accessToken,
								RegisterNotionRequestDto requestDto,
								User user) {

		// 유효한 accessToken과 pageId를 입력했는지 검증
		String pageTitle = notionProvider.getPageTitle(accessToken, requestDto.pageId());

		// notionAccount가 이미 존재하다면 그대로 가져오고 아니면 새로 생성
		NotionAccount notionAccount = notionAccountJpaRepository
			.findByUserIdAndAccessToken(user.getUserId(), accessToken)
			.orElseGet(() -> {
				NotionAccount createNotionAccount = NotionAccount.builder()
					.token(accessToken)
					.user(user)
					.build();
				return notionAccountJpaRepository.save(createNotionAccount);
			});

		if (notionPageJpaRepository.existsByPageIdAndNotionAccount(requestDto.pageId(), notionAccount)) {
			throw new Exception400(NotionExceptionStatus.NOTION_ALREADY_EXIST);
		}

		// 노션 연동 워크스페이스가 있다면 해당 워크스페이스에 카테고리 생성 없으면 노션 워크스페이스 추가
		Workspace notionWorkspace = workspaceReadService.findWorkspaceByUserAndProvider(user, LinkProvider.NOTION)
			.orElseGet(() -> workspaceSaveService.createNotionWorkspace(DEFAULT_WORKSPACE_NAME, user));

		// 초기 카테고리의 이름은 노션 페이지의 ID로 지정한다.
		Category notionCategory = categoryService.findByWorkspaceIdAndCategoryName(notionWorkspace.getId(),
			pageTitle).orElseGet(() -> categoryService.save(pageTitle, notionWorkspace));

		// notionPage insert
		NotionPage notionPage = NotionPage.builder()
			.notionAccount(notionAccount)
			.pageId(requestDto.pageId())
			.category(notionCategory)
			.isActive(true) // 이후 검증 로직을 통해서 활성화 여부를 체크
			.build();
		notionPageJpaRepository.save(notionPage);
	}
}
