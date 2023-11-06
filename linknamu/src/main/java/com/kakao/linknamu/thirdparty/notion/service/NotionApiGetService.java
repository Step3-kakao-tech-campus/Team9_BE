package com.kakao.linknamu.thirdparty.notion.service;

import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;
import com.kakao.linknamu.thirdparty.notion.repository.NotionAccountJpaRepository;
import com.kakao.linknamu.thirdparty.notion.repository.NotionPageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotionApiGetService {
	private final NotionPageJpaRepository notionPageJpaRepository;
	private final NotionAccountJpaRepository notionAccountJpaRepository;

	public List<NotionPage> getActiveNotionPage() {
		return notionPageJpaRepository.findByActivePageFetchJoin();
	}
}
