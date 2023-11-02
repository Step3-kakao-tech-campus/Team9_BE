package com.kakao.linknamu.googleDocs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.googleDocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.googleDocs.entity.GooglePage;
import com.kakao.linknamu.googleDocs.repository.GooglePageJPARepository;
import com.kakao.linknamu.user.entity.User;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class GoogleDocsApiDeleteService {
	private final GooglePageJPARepository googlePageJPARepository;

	public void deleteDocsPage(User user, Long docsPageId) {
		GooglePage googlePage = googlePageJPARepository.findById(docsPageId)
			.orElseThrow(() -> new Exception404(GoogleDocsExceptionStatus.DOCS_NOT_FOUND));

		validUserAccess(googlePage.getUser(), user);
		googlePageJPARepository.delete(googlePage);
	}

	private void validUserAccess(User writer, User accesser) {
		if (!writer.getUserId().equals(accesser.getUserId())) {
			throw new Exception403(GoogleDocsExceptionStatus.DOCS_FORBIDDEN);
		}
	}
}
