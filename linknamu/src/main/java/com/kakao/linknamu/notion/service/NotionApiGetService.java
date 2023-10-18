package com.kakao.linknamu.notion.service;

import com.kakao.linknamu.notion.entity.NotionPage;
import com.kakao.linknamu.notion.repository.NotionAccountJPARepository;
import com.kakao.linknamu.notion.repository.NotionPageJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotionApiGetService {
    private final NotionPageJPARepository notionPageJPARepository;
    private final NotionAccountJPARepository notionAccountJPARepository;

    public List<NotionPage> getActiveNotionPage() {
        return notionPageJPARepository.findByActivePageFetchJoin();
    }
}
