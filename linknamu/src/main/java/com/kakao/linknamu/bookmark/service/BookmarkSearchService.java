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

    /* bookmarkSearch 동작 */
    // 1. search문자열과 일치하는 bookmarkName을 가진 모든 북마크를 리스트 형태로 가져온다.
    // 2. 그 중에서 tags에 있는 모든 tag값을 가지는 bookmark를 선택한다.
    // 이떄, tags가 null일 경우 검색된 모든 bookmark를 선택하는 것과 같다.
    // 3. 해당되는 bookmark를 result에 저장한 후 result에 있는 모든 북마크를 이용하여 dto를 생성 후 반환한다.
    public BookmarkSearchByTagDto bookmarkSearchByTag(String search, List<String> tags, User user) {

        List<Bookmark> searchedBookmarks = bookmarkTagSearchService.searchMatchingBookmarks(search, tags, user.getUserId());

        List<BookmarkResponseDto.SearchDto> response = new ArrayList<>();
        for(Bookmark resultBookmark : searchedBookmarks) {
            List<String> allTags = bookmarkTagSearchService.searchTagNamesByBookmarkId(resultBookmark.getBookmarkId());
            response.add(BookmarkResponseDto.SearchDto.builder()
                    .bookmarkId(resultBookmark.getBookmarkId())
                    .title(resultBookmark.getBookmarkName())
                    .description(resultBookmark.getBookmarkDescription())
                    .url(resultBookmark.getBookmarkLink())
                    .imageUrl(resultBookmark.getBookmarkThumbnail())
                    .tags(allTags)
                    .build());
        }

        List<BookmarkSearchByTagDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
        for (Bookmark resultBookmark : searchedBookmarks) {
            List<Tag> bookmarkTags = bookmarkTagSearchService.findTagsByBookmarkId(resultBookmark.getBookmarkId());
            bookmarkContentDtos.add(BookmarkSearchByTagDto.BookmarkContentDto.of(resultBookmark,bookmarkTags));
        }
        return BookmarkSearchByTagDto.of(bookmarkContentDtos);
    }
}
