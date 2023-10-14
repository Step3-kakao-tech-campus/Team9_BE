package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagDeleteService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSaveService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.service.TagSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkMoveService {
    private final BookmarkJPARepository bookmarkJPARepository;
    private final BookmarkDeleteService bookmarkDeleteService;
    private final CategoryService categoryService;
    private final BookmarkTagSearchService bookmarkTagSearchService;
    private final BookmarkTagSaveService bookmarkTagSaveService;
    private final BookmarkTagDeleteService bookmarkTagDeleteService;
    private final TagSearchService tagSearchService;

    @Transactional
    public void bookmarkMove(BookmarkRequestDto.bookmarkMoveRequestDto dto, Long userId) {
        Category toCategory = categoryService.findByIdFetchJoinWorkspace(dto.toCategoryId());
        List<Bookmark> requestedBookmarks = bookmarkJPARepository.searchRequiredBookmarks(dto.bookmarkIdList());
        Set<Long> examineSet = new HashSet<>();

        for(Bookmark b : requestedBookmarks) {
            if (!b.getCategory().getWorkspace().getUser().getUserId().equals(userId)){
                throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
            }
            examineSet.add(b.getBookmarkId());
        }

        validExistRequest(examineSet, new HashSet<>(dto.bookmarkIdList()));

        for (Bookmark b : requestedBookmarks) {
            b.moveCategory(toCategory);
        }
    }

    // 요청 북마크들이 모두 실제로 디비에 존재하는 북마크인지 체크
    private void validExistRequest(Set<Long> examineSet, Set<Long> requestedSet) {
        requestedSet.removeAll(examineSet);
        if(!requestedSet.isEmpty()) {
            log.error(requestedSet.toString());
            throw new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND);
        }
    }
}
