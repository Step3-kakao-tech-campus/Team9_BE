package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
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
public class BookmarkJPARepositoryTest {
    @Autowired
    UserJPARepository userJPARepository;
    @Autowired
    CategoryJPARepository categoryJPARepository;
    @Autowired
    BookmarkJPARepository bookmarkJPARepository;

    @Test
    @DisplayName("북마크_생성_테스트")
    public void BookmarkSaveTest() {
        // given
        User user = User.builder()
                .email("testemail@pusan.ac.kr")
                .password("testpassword")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);

        Category category = Category.builder()
                .categoryName("Category")
                .parentCategory(null)
                .user(user)
                .build();
        Category rootCategory = categoryJPARepository.save(category);

        Bookmark bookmark = Bookmark.builder()
                .bookmarkName("testbookmark")
                .bookmarkLink("https://testbookmark.com")
                .bookmarkDescription("description of testbookmark")
                .bookmarkThumbnail("Thumbnail of testbookmark")
                .category(rootCategory)
                .build();

        // when
        Bookmark savedBookmark = bookmarkJPARepository.save(bookmark);

        // then
        assertThat(bookmark).isEqualTo(savedBookmark);
        assertThat(bookmark.getBookmarkId()).isSameAs(savedBookmark.getBookmarkId());
        assertThat(bookmark.getCategory().getCategoryId()).isEqualTo(savedBookmark.getCategory().getCategoryId());
        assertThat(bookmark.getBookmarkName()).isEqualTo(savedBookmark.getBookmarkName());
        assertThat(bookmark.getBookmarkDescription()).isEqualTo(savedBookmark.getBookmarkDescription());
        assertThat(bookmark.getBookmarkLink()).isEqualTo(savedBookmark.getBookmarkLink());
        assertThat(bookmark.getBookmarkThumbnail()).isEqualTo(savedBookmark.getBookmarkThumbnail());
    }

    @Test
    @DisplayName("북마크_조회_테스트")
    public void BookmarkSearchTest() {
        // given
        User user = User.builder()
                .email("testemail@pusan.ac.kr")
                .password("testpassword")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);

        Category category = Category.builder()
                .categoryName("Category")
                .parentCategory(null)
                .user(user)
                .build();
        category = categoryJPARepository.save(category);

        Bookmark bookmark = Bookmark.builder()
                .bookmarkName("testbookmark")
                .bookmarkLink("https://testbookmark.com")
                .bookmarkDescription("description of testbookmark")
                .bookmarkThumbnail("Thumbnail of testbookmark")
                .category(category)
                .build();
        bookmark = bookmarkJPARepository.save(bookmark);

        // when
        Bookmark findById = bookmarkJPARepository.findById(bookmark.getBookmarkId()).orElseThrow();

        // then
        assertThat(findById.getBookmarkLink()).isEqualTo(bookmark.getBookmarkLink());
        assertThat(findById.getBookmarkName()).isEqualTo(bookmark.getBookmarkName());
    }
}
