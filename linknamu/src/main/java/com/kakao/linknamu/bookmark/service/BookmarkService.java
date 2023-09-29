package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSaveService;
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
public class BookmarkService {
    private final BookmarkJPARepository bookmarkJPARepository;
    private final CategoryJPARepository categoryJPARepository;
    private final TagSearchService tagSearchService;
    private final TagSaveService tagSaveService;
    private final BookmarkTagSaveService bookmarkTagSaveService;

    @Transactional
    public void bookmarkAdd(BookmarkRequestDto.bookmarkAddDTO dto) {
        /* Bookmark 테이블에 bookmark 항목 추가 */
        Category category = categoryJPARepository.findById(dto.getCategoryId()).orElseThrow(
                // 예외처리 구현
        );
        Bookmark bookmark = dto.toBookmarkEntity(category);
        try{
            bookmarkJPARepository.save(bookmark);
        } catch (Exception e) {
            // 예외처리 구현
        }

        /* 새로운 Tag일 경우 Tag 테이블에 등록해야 한다. */
        List<Tag> tagEntities = new ArrayList<>();
        User user = category.getUser();
        for(String tagName : dto.getTags()){
            // 해당 태그가 존재하지 않는다면 새롭게 생성해야 한다.
            // 생성된 태그는 태그 테이블에 등록되어야 한다.
            Tag tag = tagSearchService.searchByTagName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .user(user)
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

    public BookmarkResponseDto.SearchDto bookmarkSearch(String search, List<String> tags) {
        // search, tags를 바탕으로 DB 쿼리를 통해 정보 가져오기
        // 먼저, search문자열과 일치하는 bookmarkName을 탐색하여 List<Bookmark>로 가져온다.
        List<Bookmark> searchedBookmarks = bookmarkJPARepository.findByBookmarkName(search).orElseThrow(
                // 예외처리 구현
        );
        // searchedBookmarks중에서 태그가 tags에 속한 것을 선별한다.
        // 북마크 Id를 사용하여 북마크-태그 테이블에서 태그 Id값(여러 개일 수 있다)을 얻는다.
            // 북마크-태그 테이블의 서비스 계층에 접근하여 얻어온다.

        // 태그 Id들을 사용하여 태그 테이블에서 해당되는 태그 제목을 얻는다.
        // 태그 제목이 tags에 포함된다면 이는 찾고자 하는 북마크이므로 해당 북마크 정보를 반환한다.
        return BookmarkResponseDto.SearchDto.builder()
                .bookmarkId()
                .title()
                .description()
                .url()
                .imageUrl()
                .tags()
                .createdAt()
                .build();
    }
}
