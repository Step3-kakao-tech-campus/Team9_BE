package com.kakao.linknamu.thirdparty.notion.service;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.thirdparty.notion.NotionExceptionStatus;
import com.kakao.linknamu.thirdparty.notion.dto.NotionKafkaReqeusetDto;
import com.kakao.linknamu.thirdparty.notion.dto.RegisterNotionRequestDto;
import com.kakao.linknamu.thirdparty.notion.entity.NotionAccount;
import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;
import com.kakao.linknamu.thirdparty.notion.repository.NotionAccountJpaRepository;
import com.kakao.linknamu.thirdparty.notion.repository.NotionPageJpaRepository;
import com.kakao.linknamu.thirdparty.notion.util.NotionProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class NotionApiService {
	private final NotionAccountJpaRepository notionAccountJpaRepository;
	private final NotionPageJpaRepository notionPageJpaRepository;
	private final CategoryService categoryService;
	private final WorkspaceService workspaceService;
	private final NotionProvider notionProvider;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper om;

	private static final String DEFAULT_WORKSPACE_NAME = "Notion 연동";

	/*
		로직
		1. 입력 accessToken이 유효한지 판단
		2. noticeAccount가 없다면 새로 생성
		3. 기존에 등록한 적이 있는지 확인 -> 있다면 예외처리
		4. 링크들을 연동할 워크스페이스 및 카테고리 생성
		5. noticePage 생성
		6. 해당 페이지를 파싱하는 Task를 다른 쓰레드에게 위임 후 종료
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
		Workspace notionWorkspace = workspaceService.findWorkspaceByUserAndProvider(DEFAULT_WORKSPACE_NAME, user,
			LinkProvider.NOTION);

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

		notionRequestToKafka(notionPage, notionAccount);
	}

	public void deleteNotionAccount(User user, Long notionAccountId) {
		NotionAccount notionAccount = notionAccountJpaRepository.findById(notionAccountId)
			.orElseThrow(() -> new Exception404(NotionExceptionStatus.NOTION_ACCOUNT_NOT_FOUND));
		validUser(notionAccount, user);
		notionAccountJpaRepository.delete(notionAccount);
	}

	private void validUser(NotionAccount notionAccount, User user) {
		if (!notionAccount.getUser().getUserId().equals(user.getUserId())) {
			throw new Exception403(NotionExceptionStatus.NOTION_FORBIDDEN);
		}
	}

	// 초기 노션 연동 생성 시 데이터를 가져오는 것을 다른 쓰레드에 위임
	private void notionRequestToKafka(NotionPage notionPage, NotionAccount notionAccount) {
		NotionKafkaReqeusetDto requestDto = NotionKafkaReqeusetDto.builder()
			.pageId(notionPage.getPageId())
			.accessToken(notionAccount.getToken())
			.categoryId(notionPage.getCategory().getCategoryId())
			.userId(notionAccount.getUser().getUserId())
			.build();

		try {
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
				"notion",
				om.writeValueAsString(requestDto)
			);
		} catch (JsonProcessingException ignored) {
		}
	}
}
