package com.kakao.linknamu.thirdparty.notion.service;

import com.kakao.linknamu.thirdparty.notion.NotionExceptionStatus;
import com.kakao.linknamu.thirdparty.notion.entity.NotionAccount;
import com.kakao.linknamu.thirdparty.notion.repository.NotionAccountJPARepository;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class NotionApiDeleteService {
	private final NotionAccountJPARepository notionAccountJPARepository;

	public void deleteNotionAccount(User user, Long notionAccountId) {
		NotionAccount notionAccount = notionAccountJPARepository.findById(notionAccountId)
			.orElseThrow(() -> new Exception404(NotionExceptionStatus.NOTION_ACCOUNT_NOT_FOUND));

		validUserAccess(notionAccount.getUser(), user);

		notionAccountJPARepository.delete(notionAccount);
	}

	private void validUserAccess(User writer, User accesser) {
		if (!writer.getUserId().equals(accesser.getUserId())) {
			throw new Exception403(NotionExceptionStatus.NOTION_FORBIDDEN);
		}
	}
}
