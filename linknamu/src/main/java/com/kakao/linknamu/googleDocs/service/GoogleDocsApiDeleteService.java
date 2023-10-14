package com.kakao.linknamu.googleDocs.service;

import com.kakao.linknamu.googleDocs.repository.GooglePageJPARepository;
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

    }

}
