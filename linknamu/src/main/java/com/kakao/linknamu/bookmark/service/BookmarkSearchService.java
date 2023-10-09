package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.dto.PageInfoDto;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchDto;
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

    public BookmarkSearchDto bookmarkSearchByQueryDsl(BookmarkSearchCondition condition, User user, Pageable pageable) {
        Page<Bookmark> searchedBookmarks;
        searchedBookmarks = bookmarkTagSearchService.searchByQueryDsl(
                condition,
                user.getUserId(),
                isNull(condition.tags()) ? 0L : (long) condition.tags().size(),
                pageable);

        List<BookmarkSearchDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
        for (Bookmark resultBookmark : searchedBookmarks) {
            List<Tag> bookmarkTags = bookmarkTagSearchService.findTagsByBookmarkId(resultBookmark.getBookmarkId());
            bookmarkContentDtos.add(BookmarkSearchDto.BookmarkContentDto.of(resultBookmark,bookmarkTags));
        }
        return BookmarkSearchDto.of(PageInfoDto.of(searchedBookmarks), bookmarkContentDtos);
    }
}
