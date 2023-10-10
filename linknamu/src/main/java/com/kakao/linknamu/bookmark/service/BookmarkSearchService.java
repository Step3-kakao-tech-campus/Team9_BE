package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.dto.PageInfoDto;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchResponseDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkSearchService {

    private final BookmarkJPARepository bookmarkJPARepository;
    private final BookmarkTagSearchService bookmarkTagSearchService;

    public BookmarkSearchResponseDto bookmarkSearchByQueryDsl(BookmarkSearchCondition condition, User user, Pageable pageable) {
        Page<Bookmark> searchedBookmarks;
        if (isNull(condition.tags())) searchedBookmarks = bookmarkJPARepository.search(condition, user.getUserId(), pageable);
        else searchedBookmarks = bookmarkTagSearchService.searchByQueryDsl(condition, user.getUserId(), pageable);

        List<BookmarkSearchResponseDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
        for (Bookmark resultBookmark : searchedBookmarks) {
            List<Tag> bookmarkTags = bookmarkTagSearchService.findTagsByBookmarkId(resultBookmark.getBookmarkId());
            bookmarkContentDtos.add(BookmarkSearchResponseDto.BookmarkContentDto.of(resultBookmark,bookmarkTags));
        }
        return BookmarkSearchResponseDto.of(PageInfoDto.of(searchedBookmarks), bookmarkContentDtos);
    }
}
