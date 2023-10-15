package com.kakao.linknamu.bookmarkTag.service;

import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.repository.BookmarkTagJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkTagSaveService {
    private final BookmarkTagJPARepository bookmarkTagJPARepository;

    public void createPairs(List<BookmarkTag> bookmarkTagList) {
        bookmarkTagJPARepository.saveAll(bookmarkTagList);
    }
}
