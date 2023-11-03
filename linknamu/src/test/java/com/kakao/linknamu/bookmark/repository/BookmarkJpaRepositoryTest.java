package com.kakao.linknamu.bookmark.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJpaRepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJPARepository;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookmarkJpaRepositoryTest {
	@Autowired
	UserJPARepository userJPARepository;
	@Autowired
	CategoryJpaRepository categoryJPARepository;
	@Autowired
	BookmarkJpaRepository bookmarkJPARepository;
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
		Bookmark findById = bookmarkJPARepository.findByIdFetchJoinCategoryAndWorkspace(bookmark.getBookmarkId())
			.orElseThrow();

		// then
		assertThat(findById.getBookmarkLink()).isEqualTo(bookmark.getBookmarkLink());
		assertThat(findById.getBookmarkName()).isEqualTo(bookmark.getBookmarkName());
	}

	@Test
	@DisplayName("북마크_검색_테스트(북마크이름)")
	public void searchBookmarkByNameTest() {
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

		Category category = Category.builder()
			.categoryName("category")
			.workspace(workspace1)
			.build();
		category = categoryJPARepository.save(category);

		Bookmark bookmark1 = Bookmark.builder()
			.bookmarkName("구글 번역기")
			.bookmarkLink("https://translate.google.co.kr/")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark1);
		Bookmark bookmark2 = Bookmark.builder()
			.bookmarkName("플라토")
			.bookmarkLink("https://plato.pusan.ac.kr/")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark2);
		Bookmark bookmark3 = Bookmark.builder()
			.bookmarkName("맞춤법 검사기")
			.bookmarkLink("http://speller.cs.pusan.ac.kr/")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark3);

		// when
		BookmarkSearchCondition searchCondition = BookmarkSearchCondition.builder()
			.bookmarkName("구글")
			.build();
		Page<Bookmark> result = bookmarkJPARepository.search(searchCondition, user.getUserId(), PageRequest.of(0, 10));

		// then
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent().get(0).getBookmarkName()).isEqualTo("구글 번역기");
	}

	@Test
	@DisplayName("북마크_검색_테스트(북마크링크)")
	public void searchBookmarkByLinkTest() {
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

		Category category = Category.builder()
			.categoryName("category")
			.workspace(workspace1)
			.build();
		category = categoryJPARepository.save(category);

		Bookmark bookmark1 = Bookmark.builder()
			.bookmarkName("구글 번역기")
			.bookmarkLink("https://translate.google.co.kr/")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark1);
		Bookmark bookmark2 = Bookmark.builder()
			.bookmarkName("플라토")
			.bookmarkLink("https://plato.pusan.ac.kr/")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark2);
		Bookmark bookmark3 = Bookmark.builder()
			.bookmarkName("맞춤법 검사기")
			.bookmarkLink("http://speller.cs.pusan.ac.kr/")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark3);

		// when
		BookmarkSearchCondition searchCondition = BookmarkSearchCondition.builder()
			.bookmarkLink("pusan")
			.build();
		Page<Bookmark> result = bookmarkJPARepository.search(searchCondition, user.getUserId(), PageRequest.of(0, 10));

		// then
		assertThat(result.getTotalElements()).isEqualTo(2);
		assertThat(result.getContent().get(0).getBookmarkName()).isEqualTo("맞춤법 검사기");
		assertThat(result.getContent().get(1).getBookmarkName()).isEqualTo("플라토");
	}

	@Test
	@DisplayName("북마크_검색_테스트(북마크설명)")
	public void searchBookmarkByDescriptionTest() {
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

		Category category = Category.builder()
			.categoryName("category")
			.workspace(workspace1)
			.build();
		category = categoryJPARepository.save(category);

		Bookmark bookmark1 = Bookmark.builder()
			.bookmarkName("bookmark1")
			.bookmarkLink("link1")
			.bookmarkDescription("first bookmark")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark1);
		Bookmark bookmark2 = Bookmark.builder()
			.bookmarkName("bookmark2")
			.bookmarkLink("link2")
			.bookmarkDescription("second bookmark")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark2);
		Bookmark bookmark3 = Bookmark.builder()
			.bookmarkName("bookmark3")
			.bookmarkLink("link3")
			.bookmarkDescription("third bookmark")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark3);

		// when
		BookmarkSearchCondition searchCondition = BookmarkSearchCondition.builder()
			.bookmarkDescription("first")
			.build();
		Page<Bookmark> result = bookmarkJPARepository.search(searchCondition, user.getUserId(), PageRequest.of(0, 10));

		// then
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent().get(0).getBookmarkName()).isEqualTo("bookmark1");
	}

	@Test
	@DisplayName("북마크_검색_테스트(워크스페이스이름)")
	public void searchBookmarkByWorkspaceNameTest() {
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

		Workspace workspace2 = Workspace.builder()
			.workspaceName("workspace2")
			.user(user)
			.linkProvider(LinkProvider.NORMAL)
			.build();
		workspace1 = workspaceJPARepository.save(workspace2);

		Category category2 = Category.builder()
			.categoryName("category2")
			.workspace(workspace2)
			.build();
		category2 = categoryJPARepository.save(category2);

		Bookmark bookmark1 = Bookmark.builder()
			.bookmarkName("bookmark1")
			.bookmarkLink("link1")
			.category(category1)
			.build();
		bookmarkJPARepository.save(bookmark1);
		Bookmark bookmark2 = Bookmark.builder()
			.bookmarkName("bookmark2")
			.bookmarkLink("link2")
			.category(category2)
			.build();
		bookmarkJPARepository.save(bookmark2);

		// when
		BookmarkSearchCondition searchCondition = BookmarkSearchCondition.builder()
			.workspaceName("workspace1")
			.build();
		Page<Bookmark> result = bookmarkJPARepository.search(searchCondition, user.getUserId(), PageRequest.of(0, 10));

		// then
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent().get(0).getBookmarkName()).isEqualTo("bookmark1");
	}
}
