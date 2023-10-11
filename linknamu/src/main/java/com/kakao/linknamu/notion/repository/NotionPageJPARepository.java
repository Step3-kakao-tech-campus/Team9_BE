package com.kakao.linknamu.notion.repository;

import com.kakao.linknamu.notion.entity.NotionAccount;
import com.kakao.linknamu.notion.entity.NotionPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotionPageJPARepository extends JpaRepository<NotionPage, Long> {

    boolean existsByPageIdAndNotionAccount(String pageId, NotionAccount notionAccount);

    @Query(value = "select n from NotionPage n " +
            "join fetch n.notionAccount na " +
            "where n.isActive=true")
    List<NotionPage> findByActivePageFetchJoin();
}
