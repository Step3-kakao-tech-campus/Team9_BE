package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
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
    @Autowired
    WorkspaceJPARepository workspaceJPARepository;

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

        Workspace workspace = Workspace.builder()
                .workspaceName("테스트 워크스페이스")
                .user(user)
                .linkProvider(LinkProvider.NORMAL)
                .build();
        workspaceJPARepository.save(workspace);

        Category category = Category.builder()
                .categoryName("Category")
                .workspace(workspace)
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
    @DisplayName("북마크_조회_조인_테스트")
    public void BookmarkSearchFetchJoinTest() {
        // given
        User user = User.builder()
                .email("testemail@pusan.ac.kr")
                .password("testpassword")
                .provider(Provider.PROVIDER_NORMAL)
                .role(Role.ROLE_USER)
                .build();
        user = userJPARepository.save(user);

        Workspace workspace = Workspace.builder()
                .workspaceName("테스트 워크스페이스")
                .user(user)
                .linkProvider(LinkProvider.NORMAL)
                .build();

        workspaceJPARepository.save(workspace);

        Category category = Category.builder()
                .categoryName("Category")
                .workspace(workspace)
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
        Bookmark findById = bookmarkJPARepository.findByIdFetchJoinCategoryAndWorkspace(bookmark.getBookmarkId()).orElseThrow();

        // then
        assertThat(findById.getBookmarkLink()).isEqualTo(bookmark.getBookmarkLink());
        assertThat(findById.getBookmarkName()).isEqualTo(bookmark.getBookmarkName());
    }

    @Test
    @DisplayName("북마크_검색_테스트")
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

        for (int i=1; i<=5; i++){
            Bookmark bookmark = Bookmark.builder()
                    .bookmarkName("bookmark" + i)
                    .bookmarkLink("bookmark_link" + i)
                    .bookmarkDescription("bookmark_description" + i)
                    .category(category1)
                    .build();
            bookmarkJPARepository.save(bookmark);
        }
        for (int i=1; i<=5; i++){
            Bookmark bookmark = Bookmark.builder()
                    .bookmarkName("linknamu" + i)
                    .bookmarkLink("linknamu_link" + i)
                    .bookmarkDescription("linknamu_description" + i)
                    .category(category1)
                    .build();
            bookmarkJPARepository.save(bookmark);
        }

        // when
        BookmarkSearchCondition searchCondition = BookmarkSearchCondition.builder()
                .bookmarkName("linknamu")
                .build();
        Page<Bookmark> result = bookmarkJPARepository.search(searchCondition, user.getUserId(), PageRequest.of(0, 50));

        // then
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getContent().get(0).getBookmarkName()).isEqualTo("linknamu5");
        assertThat(result.getContent().get(0).getBookmarkLink()).isEqualTo("linknamu_link5");
        assertThat(result.getContent().get(0).getBookmarkDescription()).isEqualTo("linknamu_description5");
    }
}
