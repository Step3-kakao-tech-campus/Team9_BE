package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagDeleteService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import com.kakao.linknamu.tag.service.TagDeleteService;
import com.kakao.linknamu.tag.service.TagSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookmarkDeleteService {
    private final BookmarkJPARepository bookmarkJPARepository;
    private final BookmarkTagSearchService bookmarkTagSearchService;
    private final BookmarkTagDeleteService bookmarkTagDeleteService;
    private final TagSearchService tagSearchService;
    private final TagDeleteService tagDeleteService;
    /* bookmarkDelete 동작 */
    // 1. id에 해당하는 북마크 탐색
    // 2. 해당 북마크의 태그 id검색 -> 태그 id를 사용하여 태그 제목 얻기 -> 해당 태그 제목을 가지는 태그의 개수가 1개 이하라면 해당 태그 삭제
    // 3. 북마크-태그 삭제
    // 4. 북마크 삭제
    @Transactional
    public void bookmarkDelete(Long userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkJPARepository.findByIdFetchJoinCategoryAndWorkspace(bookmarkId).orElseThrow(
                () -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND)
        );
        if(!bookmark.getCategory().getWorkspace().getUser().getUserId().equals(userId)) {
            throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
        }
        List<Long> tagIds = bookmarkTagSearchService.searchTagIdByBookmarkId(bookmarkId);
        List<String> tagNames = new ArrayList<>();
        for(Long tag : tagIds) {
            tagNames.add(tagSearchService.searchTagNameById(tag));
        }
        for(String name : tagNames) {
//            List<Long> idsSearchedByName = tagSearchService.searchTagIdsByName(name);
            bookmarkTagDeleteService.deleteBookmarkTag(bookmarkId, name);
//            if(idsSearchedByName.size() <= 1) {
//                tagDeleteService.deleteTagByName(userId, name);
//            }
        }
        bookmarkJPARepository.delete(bookmark);
    }
}
