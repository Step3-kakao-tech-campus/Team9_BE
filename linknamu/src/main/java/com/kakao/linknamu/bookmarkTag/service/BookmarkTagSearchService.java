package com.kakao.linknamu.bookmarkTag.service;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmarkTag.repository.BookmarkTagJPARepository;
import com.kakao.linknamu.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkTagSearchService {
    private final BookmarkTagJPARepository bookmarkTagJPARepository;

    public List<String> searchTagNamesByBookmarkId(Long bookmarkId) {
        return bookmarkTagJPARepository.findTagNamesByBookmarkId(bookmarkId);
    }

    public List<Long> searchTagIdsByBookmarkId(Long bookmarkId) {
        return bookmarkTagJPARepository.findTagIdsByBookmarkId(bookmarkId);
    }

    public List<Tag> findTagsByBookmarkId(Long bookmarkId){
        return bookmarkTagJPARepository.findTagByBookmarkId(bookmarkId);
    }

    public Page<Bookmark> searchByQueryDsl(BookmarkSearchCondition condition, Long userId, Long count, Pageable pageable) {
        return bookmarkTagJPARepository.search(condition, userId, count, pageable);
    }
}
