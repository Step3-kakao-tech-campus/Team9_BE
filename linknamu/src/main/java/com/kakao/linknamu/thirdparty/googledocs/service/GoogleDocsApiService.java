package com.kakao.linknamu.thirdparty.googledocs.service;

import static com.kakao.linknamu.core.util.KafkaTopics.*;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.thirdparty.googledocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.thirdparty.googledocs.dto.GoogleDocsKafkaRequestDto;
import com.kakao.linknamu.thirdparty.googledocs.dto.RegisterGoogleDocsRequestDto;
import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googledocs.repository.GooglePageJpaRepository;
import com.kakao.linknamu.thirdparty.googledocs.util.GoogleDocsProvider;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper om;

	private static final String DEFAULT_WORKSPACE_NAME = "Google Docs";

	/*
		로직
		1. 입력 문서ID가 유효한지 판단
		2. 기존에 등록한 적이 있는지 확인 -> 있다면 예외처리
		3. 링크들을 연동할 워크스페이스 및 카테고리 생성
		4. GooglePage 생성
		5. 해당 페이지를 파싱하는 Task를 다른 쓰레드에게 위임 후 종료
	 */
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

		googleDocsRequestToKafka(googlePage);
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

	// 초기 구글문서 연동 생성 시 데이터를 가져오는 것을 다른 쓰레드에 위임
	private void googleDocsRequestToKafka(GooglePage googlePage) {
		try {
			GoogleDocsKafkaRequestDto googleDocsKafkaRequestDto = GoogleDocsKafkaRequestDto.builder()
				.documentId(googlePage.getDocumentId())
				.categoryId(googlePage.getCategory().getCategoryId())
				.build();
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
				GOOGLE_DOCS_TOPIC,
				om.writeValueAsString(googleDocsKafkaRequestDto)
			);
		} catch (JsonProcessingException ignored) {
		}

	}
}
