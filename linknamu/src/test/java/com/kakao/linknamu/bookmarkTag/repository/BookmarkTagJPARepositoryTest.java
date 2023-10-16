package com.kakao.linknamu.bookmarkTag.repository;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
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
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    WorkspaceJPARepository workspaceJPARepository;

    @Test
    @DisplayName("북마크-태그_생성_테스트")
    public void BookmarkTagSaveTest() {
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
                .build();
        Category rootCategory = categoryJPARepository.save(category);

        Bookmark bookmark = Bookmark.builder()
                .bookmarkName("testbookmark")
                .bookmarkLink("https://testbookmark.com")
                .bookmarkDescription("description of testbookmark")
                .bookmarkThumbnail("Thumbnail of testbookmark")
                .category(rootCategory)
                .build();
        bookmarkJPARepository.save(bookmark);

        Tag tag = Tag.builder()
                .tagName("testTag")
                .user(user)
                .build();
        tagJPARepository.save(tag);

        BookmarkTag bookmarkTag = BookmarkTag.builder()
                .bookmark(bookmark)
                .tag(tag)
                .build();

        // when
        BookmarkTag savedBookmarkTag = bookmarkTagJPARepository.save(bookmarkTag);

        // then
        assertThat(bookmarkTag).isEqualTo(savedBookmarkTag);
        assertThat(savedBookmarkTag.getBookmark().getBookmarkId()).isEqualTo(bookmark.getBookmarkId());
        assertThat(savedBookmarkTag.getTag().getTagId()).isEqualTo(tag.getTagId());
    }

    @Test
    @DisplayName("북마크태그_검색_테스트")
    public void searchTest() {
        // given
        User user = User.builder()
                .email("grindabff@pusan.ac.kr")
                .password("password")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);

        Workspace workspace1 = Workspace.builder()
                .workspaceName("workspace1")
                .user(user)
                .linkProvider(LinkProvider.NORMAL)
                .build();
        workspace1 = workspaceJPARepository.save(workspace1);

        Category category1 = Category.builder()
                .categoryName("category1")
                .workspace(workspace1)
                .build();
        category1 = categoryJPARepository.save(category1);

        Tag tag = Tag.builder()
                .tagName("tag")
                .user(user)
                .build();
        tag = tagJPARepository.save(tag);

        for (int i=1; i<=10; i++){
            Bookmark bookmark = Bookmark.builder()
                    .bookmarkName("bookmark" + i)
                    .bookmarkLink("bookmark_link" + i)
                    .bookmarkDescription("bookmark_description" + i)
                    .category(category1)
                    .build();
            bookmarkJPARepository.save(bookmark);

            if (i%2 == 0) {
                BookmarkTag bookmarkTag = BookmarkTag.builder()
                        .bookmark(bookmark)
                        .tag(tag)
                        .build();
                bookmarkTagJPARepository.save(bookmarkTag);
            }
        }

        // when
        BookmarkSearchCondition searchCondition = BookmarkSearchCondition.builder()
                .tags(List.of("tag"))
                .build();
        Page<Bookmark> result = bookmarkTagJPARepository.search(searchCondition, user.getUserId(), PageRequest.of(0, 50));

        // then
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getContent().get(0).getBookmarkName()).isEqualTo("bookmark10");
        assertThat(result.getContent().get(0).getBookmarkLink()).isEqualTo("bookmark_link10");
        assertThat(result.getContent().get(0).getBookmarkDescription()).isEqualTo("bookmark_description10");

    }
}
