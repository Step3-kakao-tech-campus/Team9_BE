package com.kakao.linknamu.bookmarkTag.service;

import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmarkTag.BookmarkTagExceptionStatus;
import com.kakao.linknamu.bookmarkTag.repository.BookmarkTagJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkTagSearchService {
    private final BookmarkTagJPARepository bookmarkTagJPARepository;

    public List<String> searchNamesByBookmarkId(Long bookmarkId) {
        return bookmarkTagJPARepository.findTagNamesByBookmarkId(bookmarkId);
    }

    public List<Long> searchTagIdByBookmarkId(Long bookmarkId) {
        return bookmarkTagJPARepository.findTagIdByBookmarkId(bookmarkId);
    }

    public List<Bookmark> searchMatchingBookmarks(String search, List<String> tags) {
        return bookmarkTagJPARepository.findMatchingBookmarks(search, tags).orElseThrow(
                () -> new Exception404(BookmarkTagExceptionStatus.BOOKMARK_TAG_NOT_FOUND)
        );
    }
}
