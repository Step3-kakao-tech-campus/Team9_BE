package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSaveService;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.service.TagSaveService;
import com.kakao.linknamu.tag.service.TagSearchService;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkCreateService {
    private final BookmarkJPARepository bookmarkJPARepository;
    private final CategoryJPARepository categoryJPARepository;
    private final TagSearchService tagSearchService;
    private final TagSaveService tagSaveService;
    private final BookmarkTagSaveService bookmarkTagSaveService;

    @Transactional
    public void bookmarkAdd(BookmarkRequestDto.BookmarkAddDTO bookmarkAddDTO, User userDetails) {
        /* Bookmark 테이블에 bookmark 항목 추가 */
        Category category = categoryJPARepository.findByIdFetchJoinWorkspace(bookmarkAddDTO.getCategoryId()).orElseThrow(
                () -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
        );
        if(!category.getWorkspace().getUser().getUserId().equals(userDetails.getUserId())){
            throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
        }
        // 북마크의 링크에 대한 중복 검사
        bookmarkJPARepository.findByCategoryIdAndBookmarkLink(category.getCategoryId(), bookmarkAddDTO.getBookmarkLink()).ifPresent((b) -> {
            throw new Exception400(BookmarkExceptionStatus.BOOKMARK_ALREADY_EXISTS);
        });

        Bookmark bookmark = bookmarkAddDTO.toBookmarkEntity(category);
        bookmarkJPARepository.save(bookmark);

        /* 새로운 Tag일 경우 Tag 테이블에 등록해야 한다. */
        List<Tag> tagEntities = new ArrayList<>();
        for(String tagName : bookmarkAddDTO.getTags()){
            // 해당 태그가 존재하지 않는다면 새롭게 생성해야 한다.
            // 생성된 태그는 태그 테이블에 등록되어야 한다.
            Tag tag = tagSearchService.searchByTagName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .user(userDetails)
                                .tagName(tagName)
                                .build();
                        tagSaveService.createTag(newTag);
                        return newTag;
                    });
            tagEntities.add(tag);
        }

        /* BookmarkTag 테이블에 등록 */
        List<BookmarkTag> bookmarkTagList = tagEntities.stream()
                .map(tag -> BookmarkTag.builder()
                        .bookmark(bookmark)
                        .tag(tag)
                        .build())
                .toList();
        bookmarkTagSaveService.createPairs(bookmarkTagList);
    }

    public void bookmarkBatchInsert(List<Bookmark> bookmarkList) {
        bookmarkJPARepository.bookmarkBatchInsert(bookmarkList);
    }
}
