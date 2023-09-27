package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu.bookmark.dto.BookmarkRequestDTO;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.BookmarkTagJPARepository;
import com.kakao.linknamu.category.Category;
import com.kakao.linknamu.category.CategoryJPARepository;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJPARepository;
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
    private final TagJPARepository tagJPARepository;
    private final BookmarkTagJPARepository bookmarkTagJPARepository;

    @Transactional
    public void bookmarkAdd(BookmarkRequestDTO.bookmarkAddDTO dto) {
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
            Tag tag = tagJPARepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .user(user)
                                .tagName(tagName)
                                .build();
                        tagJPARepository.save(newTag);
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
        bookmarkTagJPARepository.saveAll(bookmarkTagList);
    }
}
