package com.kakao.linknamu.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;

public interface BookmarkJpaRepositoryCustom {
	Page<Bookmark> search(BookmarkSearchCondition condition, Long userId, Pageable pageable);
}
