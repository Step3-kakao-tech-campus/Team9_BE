package com.kakao.linknamu.thirdparty.googledocs.service;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.thirdparty.googledocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googledocs.repository.GooglePageJpaRepository;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class GoogleDocsApiDeleteService {
	private final GooglePageJpaRepository googlePageJpaRepository;

	public void deleteDocsPage(User user, Long docsPageId) {
		GooglePage googlePage = googlePageJpaRepository.findById(docsPageId)
			.orElseThrow(() -> new Exception404(GoogleDocsExceptionStatus.DOCS_NOT_FOUND));

		validUserAccess(googlePage.getUser(), user);
		googlePageJpaRepository.delete(googlePage);
	}

	private void validUserAccess(User writer, User accesser) {
		if (!writer.getUserId().equals(accesser.getUserId())) {
			throw new Exception403(GoogleDocsExceptionStatus.DOCS_FORBIDDEN);
		}
	}
}
