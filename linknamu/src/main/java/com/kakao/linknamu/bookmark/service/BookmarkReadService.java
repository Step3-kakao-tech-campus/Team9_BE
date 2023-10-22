package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
