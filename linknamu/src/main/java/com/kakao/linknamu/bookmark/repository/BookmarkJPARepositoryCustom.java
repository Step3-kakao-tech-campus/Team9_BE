package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkJPARepositoryCustom {
    Page<Bookmark> search(BookmarkSearchCondition condition, Long userId, Pageable pageable);
}
