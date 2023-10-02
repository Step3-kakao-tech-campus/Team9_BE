package com.kakao.linknamu.bookmark;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
        assertThat(bookmark).isSameAs(savedBookmark);
        assertThat(bookmark.getBookmarkId()).isSameAs(savedBookmark.getBookmarkId());
        assertThat(bookmark.getCategory().getCategoryId()).isSameAs(savedBookmark.getCategory().getCategoryId());
        assertThat(bookmark.getBookmarkName()).isSameAs(savedBookmark.getBookmarkName());
        assertThat(bookmark.getBookmarkDescription()).isSameAs(savedBookmark.getBookmarkDescription());
        assertThat(bookmark.getBookmarkLink()).isSameAs(savedBookmark.getBookmarkLink());
        assertThat(bookmark.getBookmarkThumbnail()).isSameAs(savedBookmark.getBookmarkThumbnail());
    }
}
