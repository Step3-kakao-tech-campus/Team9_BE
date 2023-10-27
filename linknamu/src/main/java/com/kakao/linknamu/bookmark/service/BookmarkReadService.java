package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookmarkReadService {

    private final BookmarkJPARepository bookmarkJPARepository;

    public Page<Bookmark> findByCategoryId(Long categoryId, Pageable pageable) {
        return bookmarkJPARepository.findByCategoryId(categoryId, pageable);
    }


    public List<Bookmark> getBookmarkListByCategoryId(Long categoryId) {
        return bookmarkJPARepository.findListByCategoryId(categoryId);
    }

    public Bookmark getBookmarkById(Long bookmarkId) {
        return bookmarkJPARepository.findById(bookmarkId)
                .orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));
    }
}
