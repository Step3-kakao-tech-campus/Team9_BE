package com.kakao.linknamu.bookmarkTag.service;

import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.repository.BookmarkTagJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkTagDeleteService {
    private final BookmarkTagJPARepository bookmarkTagJPARepository;

    public void deleteBookmarkTag(Long bookmarkId, String tagName) {
        BookmarkTag bookmarkTag = bookmarkTagJPARepository.findMatchingBookmarkTag(bookmarkId, tagName);
        bookmarkTagJPARepository.delete(bookmarkTag);
    }
}
