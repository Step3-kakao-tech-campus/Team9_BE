package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchByTagDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkSearchService {
    private final BookmarkTagSearchService bookmarkTagSearchService;

    public BookmarkSearchByTagDto bookmarkSearchByTag(String keyword, List<String> tags, User user) {
        // 리스트의 태그를 모두 가지고 bookmarkName에 키워드를 포함하는 북마크를 조회한다
        List<Bookmark> searchedBookmarks = bookmarkTagSearchService.searchMatchingBookmarks(keyword, tags, user.getUserId());

        List<BookmarkSearchByTagDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
        for (Bookmark resultBookmark : searchedBookmarks) {
            // 북마크가 가지는 태그를 조회한다
            List<Tag> bookmarkTags = bookmarkTagSearchService.findTagsByBookmarkId(resultBookmark.getBookmarkId());
            bookmarkContentDtos.add(BookmarkSearchByTagDto.BookmarkContentDto.of(resultBookmark,bookmarkTags));
        }
        return BookmarkSearchByTagDto.of(bookmarkContentDtos);
    }
}
