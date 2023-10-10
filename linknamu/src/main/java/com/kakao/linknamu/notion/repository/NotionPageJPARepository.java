package com.kakao.linknamu.notion.repository;

import com.kakao.linknamu.notion.entity.NotionAccount;
import com.kakao.linknamu.notion.entity.NotionPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotionPageJPARepository extends JpaRepository<NotionPage, Long> {

    boolean existsByPageIdAndNotionAccount(String pageId, NotionAccount notionAccount);
}
