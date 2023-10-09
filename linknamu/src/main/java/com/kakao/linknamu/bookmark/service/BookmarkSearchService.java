package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.dto.PageInfoDto;
import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepositoryImpl;
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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkSearchService {

    private final BookmarkJPARepository bookmarkJPARepository;
    private final BookmarkTagSearchService bookmarkTagSearchService;

    public BookmarkSearchDto bookmarkSearch(String type, String keyword, List<String> tags, User user, Pageable pageable) {
        Page<Bookmark> searchedBookmarks;
        if (tags.isEmpty()) searchedBookmarks = searchWithoutTag(type, keyword, user, pageable);
        else searchedBookmarks = searchWithTag(type, keyword, tags, user, pageable);

        List<BookmarkSearchDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
        for (Bookmark resultBookmark : searchedBookmarks) {
            // 북마크가 가지는 태그를 조회한다
            List<Tag> bookmarkTags = bookmarkTagSearchService.findTagsByBookmarkId(resultBookmark.getBookmarkId());
            bookmarkContentDtos.add(BookmarkSearchDto.BookmarkContentDto.of(resultBookmark,bookmarkTags));
        }
        return BookmarkSearchDto.of(PageInfoDto.of(searchedBookmarks), bookmarkContentDtos);
    }

    public BookmarkSearchDto bookmarkSearchByQueryDsl(BookmarkSearchCondition condition, User user, Pageable pageable) {
        Page<Bookmark> searchedBookmarks;
        if (condition.tags() == null) searchedBookmarks = bookmarkJPARepository.search(condition, user.getUserId(), pageable);
        else searchedBookmarks = bookmarkTagSearchService.searchByQueryDsl(condition, user.getUserId(), (long) condition.tags().size(), pageable);

        List<BookmarkSearchDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
        for (Bookmark resultBookmark : searchedBookmarks) {
            List<Tag> bookmarkTags = bookmarkTagSearchService.findTagsByBookmarkId(resultBookmark.getBookmarkId());
            bookmarkContentDtos.add(BookmarkSearchDto.BookmarkContentDto.of(resultBookmark,bookmarkTags));
        }
        return BookmarkSearchDto.of(PageInfoDto.of(searchedBookmarks), bookmarkContentDtos);
    }

    private Page<Bookmark> searchWithoutTag(String type, String keyword, User user, Pageable pageable){
        return switch (type) {
            case "T" -> bookmarkJPARepository.searchByBookmarkName(keyword, user.getUserId(), pageable);
            case "L" -> bookmarkJPARepository.searchByBookmarkLink(keyword, user.getUserId(), pageable);
            case "D" -> bookmarkJPARepository.searchByBookmarkDescription(keyword, user.getUserId(), pageable);
            default -> throw new Exception400(BookmarkExceptionStatus.INVALID_SEARCH_TYPE);
        };
    }

    private Page<Bookmark> searchWithTag(String type, String keyword, List<String> tags, User user, Pageable pageable){
        return switch (type) {
            case "T" -> bookmarkTagSearchService.searchByBookmarkName(keyword, tags, user.getUserId(), pageable);
            case "L" -> bookmarkTagSearchService.searchByBookmarkLink(keyword, tags, user.getUserId(), pageable);
            case "D" -> bookmarkTagSearchService.searchByBookmarkDescription(keyword, tags, user.getUserId(), pageable);
            default -> throw new Exception400(BookmarkExceptionStatus.INVALID_SEARCH_TYPE);
        };
    }
}
