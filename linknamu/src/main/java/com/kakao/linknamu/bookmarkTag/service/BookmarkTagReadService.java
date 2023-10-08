package com.kakao.linknamu.bookmarkTag.service;

import com.kakao.linknamu.bookmarkTag.repository.BookmarkTagJPARepository;
import com.kakao.linknamu.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkTagReadService {

    private final BookmarkTagJPARepository bookmarkTagJPARepository;

    public List<Tag> findTagByBookmarkId(Long bookmarkId){
        return bookmarkTagJPARepository.findTagByBookmarkId(bookmarkId);
    }
}
