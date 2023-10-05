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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Category toCategory = categoryService.findById(dto.toCategoryId());
        List<Bookmark> requestedBookmarks = bookmarkJPARepository.searchRequiredBookmarks(dto.bookmarkIdList());
        Set<Long> examineSet = new HashSet<>();
        Long validateUserId = null;
        for(Bookmark b : requestedBookmarks) {
            examineSet.add(b.getBookmarkId());
            if(validateUserId == null) {
                validateUserId = b.getCategory().getWorkspace().getUser().getUserId();
            }
            else {
                if(!validateUserId.equals(b.getCategory().getWorkspace().getUser().getUserId())) {
                    throw new Exception400(BookmarkExceptionStatus.BOOKMARK_WRONG_REQUEST);
                }
            }
        }

        Set<Long> requestedIds = new HashSet<>(dto.bookmarkIdList());
        examineSet.removeAll(requestedIds);
        if(!examineSet.isEmpty()) {
            throw new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND);
        }

        for(Bookmark bookmark : requestedBookmarks) {
            Long bookmarkUserId = bookmark.getCategory().getWorkspace().getUser().getUserId();
            if(!validateUserId.equals(userId)) {
                throw new Exception403((BookmarkExceptionStatus.BOOKMARK_FORBIDDEN));
            }

            Long formalId = bookmark.getBookmarkId();
            String name = bookmark.getBookmarkName();
            String link = bookmark.getBookmarkLink();
            String description = bookmark.getBookmarkDescription();
            String thumbnail = bookmark.getBookmarkThumbnail();
            List<Long> tagIds = bookmarkTagSearchService.searchTagIdByBookmarkId(formalId);

            bookmarkDeleteService.bookmarkDelete(userId, bookmark.getBookmarkId());

            bookmark = Bookmark.builder()
                    .category(toCategory)
                    .bookmarkLink(link)
                    .bookmarkName(name)
                    .bookmarkDescription(description)
                    .bookmarkThumbnail(thumbnail)
                    .build();
            bookmarkJPARepository.save(bookmark);

            List<BookmarkTag> pairs = new ArrayList<>();
            for(Long tagId : tagIds){
                Tag tag = tagSearchService.findById(tagId);
                bookmarkTagDeleteService.deleteBookmarkTag(formalId, tag.getTagName());
                pairs.add(BookmarkTag.builder()
                                .bookmark(bookmark)
                                .tag(tag)
                                .build());
            }
            bookmarkTagSaveService.createPairs(pairs);
        }
    }
}
