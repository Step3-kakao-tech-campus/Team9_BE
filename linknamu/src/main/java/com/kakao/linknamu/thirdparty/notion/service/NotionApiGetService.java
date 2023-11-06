package com.kakao.linknamu.thirdparty.notion.service;

import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;
import com.kakao.linknamu.thirdparty.notion.repository.NotionAccountJpaRepository;
import com.kakao.linknamu.thirdparty.notion.repository.NotionPageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotionApiGetService {
	private final NotionPageJpaRepository notionPageJpaRepository;

	public List<NotionPage> getActiveNotionPage() {
		return notionPageJpaRepository.findByActivePageFetchJoin();
	}
}
