package com.kakao.linknamu.thirdparty.googleDocs.service;

import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.thirdparty.googleDocs.GoogleDocsExceptionStatus;
import com.kakao.linknamu.thirdparty.googleDocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googleDocs.repository.GooglePageJPARepository;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
