package com.kakao.linknamu.bookmarkTag.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookmarkTagJPARepositoryTest {
    @Autowired
    UserJPARepository userJPARepository;
    @Autowired
    CategoryJPARepository categoryJPARepository;
    @Autowired
    BookmarkJPARepository bookmarkJPARepository;
    @Autowired
    TagJPARepository tagJPARepository;
    @Autowired
    BookmarkTagJPARepository bookmarkTagJPARepository;

//    @Test
//    @DisplayName("북마크-태그_생성_테스트")
//    public void BookmarkTagSaveTest() {
//        // given
//        User user = User.builder()
//                .email("testemail@pusan.ac.kr")
//                .password("testpassword")
//                .provider(Provider.PROVIDER_NORMAL)
//                .role(Role.ROLE_USER)
//                .build();
//        user = userJPARepository.save(user);
//
//        Category category = Category.builder()
//                .categoryName("Category")
//                .parentCategory(null)
//                .user(user)
//                .build();
//        Category rootCategory = categoryJPARepository.save(category);
//
//        Bookmark bookmark = Bookmark.builder()
//                .bookmarkName("testbookmark")
//                .bookmarkLink("https://testbookmark.com")
//                .bookmarkDescription("description of testbookmark")
//                .bookmarkThumbnail("Thumbnail of testbookmark")
//                .category(rootCategory)
//                .build();
//        bookmarkJPARepository.save(bookmark);
//
//        Tag tag = Tag.builder()
//                .tagName("testTag")
//                .user(user)
//                .build();
//        tagJPARepository.save(tag);
//
//        BookmarkTag bookmarkTag = BookmarkTag.builder()
//                .bookmark(bookmark)
//                .tag(tag)
//                .build();
//
//        // when
//        BookmarkTag savedBookmarkTag = bookmarkTagJPARepository.save(bookmarkTag);
//
//        // then
//        assertThat(bookmarkTag).isEqualTo(savedBookmarkTag);
//        assertThat(savedBookmarkTag.getBookmark().getBookmarkId()).isEqualTo(bookmark.getBookmarkId());
//        assertThat(savedBookmarkTag.getTag().getTagId()).isEqualTo(tag.getTagId());
//    }
}
