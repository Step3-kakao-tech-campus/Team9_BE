package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagDeleteService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import com.kakao.linknamu.tag.service.TagDeleteService;
import com.kakao.linknamu.tag.service.TagSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookmarkDeleteService {
    private final BookmarkJPARepository bookmarkJPARepository;

    /* bookmarkDelete 동작 */
    // 1. id에 해당하는 북마크 탐색
    // 2. 북마크 삭제
    @Transactional
    public void bookmarkDelete(Long userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkJPARepository.findByIdFetchJoinCategoryAndWorkspace(bookmarkId).orElseThrow(
                () -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND)
        );
        if(!bookmark.getCategory().getWorkspace().getUser().getUserId().equals(userId)) {
            throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
        }

        bookmarkJPARepository.delete(bookmark);
    }
}
