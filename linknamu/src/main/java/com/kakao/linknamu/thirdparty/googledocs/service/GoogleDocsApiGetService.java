package com.kakao.linknamu.thirdparty.googledocs.service;

import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googledocs.repository.GooglePageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleDocsApiGetService {
	private final GooglePageJpaRepository googlePageJpaRepository;

	public List<GooglePage> getActiveGoogleDocsPage() {
		return googlePageJpaRepository.findByActivePage();
	}
}
