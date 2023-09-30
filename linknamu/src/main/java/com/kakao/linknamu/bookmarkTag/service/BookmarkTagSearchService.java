package com.kakao.linknamu.bookmarkTag.service;

import com.kakao.linknamu.bookmarkTag.repository.BookmarkTagJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkTagSearchService {
    private final BookmarkTagJPARepository bookmarkTagJPARepository;

    public List<String> searchIdByBookmarkId(Long bookmarkId) {
        return bookmarkTagJPARepository.findTagIdsByBookmarkId(bookmarkId);
    }
}
