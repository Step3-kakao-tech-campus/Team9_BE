package com.kakao.linknamu.googleDocs.service;

import com.kakao.linknamu.googleDocs.entity.GooglePage;
import com.kakao.linknamu.googleDocs.repository.GooglePageJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleDocsApiGetService {
    private final GooglePageJPARepository googlePageJPARepository;

    public List<GooglePage> getActiveGoogleDocsPage() {
        return googlePageJPARepository.findByActivePage();
    }
}
