package com.kakao.linknamu.thirdparty.notion.service;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.thirdparty.notion.NotionExceptionStatus;
import com.kakao.linknamu.thirdparty.notion.entity.NotionAccount;
import com.kakao.linknamu.thirdparty.notion.repository.NotionAccountJpaRepository;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class NotionApiDeleteService {
	private final NotionAccountJpaRepository notionAccountJpaRepository;

	public void deleteNotionAccount(User user, Long notionAccountId) {
		NotionAccount notionAccount = notionAccountJpaRepository.findById(notionAccountId)
			.orElseThrow(() -> new Exception404(NotionExceptionStatus.NOTION_ACCOUNT_NOT_FOUND));

		validUserAccess(notionAccount.getUser(), user);

		notionAccountJpaRepository.delete(notionAccount);
	}

	private void validUserAccess(User writer, User accesser) {
		if (!writer.getUserId().equals(accesser.getUserId())) {
			throw new Exception403(NotionExceptionStatus.NOTION_FORBIDDEN);
		}
	}
}
