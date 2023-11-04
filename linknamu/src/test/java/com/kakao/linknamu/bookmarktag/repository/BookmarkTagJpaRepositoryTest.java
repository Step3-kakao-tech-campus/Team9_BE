package com.kakao.linknamu.bookmarktag.repository;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJpaRepository;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJpaRepository;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJpaRepository;
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
public class BookmarkTagJpaRepositoryTest {
	@Autowired
	UserJpaRepository userJPARepository;
	@Autowired
	CategoryJpaRepository categoryJPARepository;
	@Autowired
	BookmarkJpaRepository bookmarkJPARepository;
	@Autowired
	TagJpaRepository tagJPARepository;
	@Autowired
	BookmarkTagJpaRepository bookmarkTagJPARepository;
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
	public void searchBookmarkByTagTest() {
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

		Tag tag1 = Tag.builder()
			.tagName("tag1")
			.user(user)
			.build();
		tag1 = tagJPARepository.save(tag1);

		Tag tag2 = Tag.builder()
			.tagName("tag2")
			.user(user)
			.build();
		tag2 = tagJPARepository.save(tag2);

		Bookmark bookmark1 = Bookmark.builder()
			.bookmarkName("bookmark1")
			.bookmarkLink("link1")
			.category(category)
			.build();
		bookmarkJPARepository.save(bookmark1);

		Bookmark bookmark2 = Bookmark.builder()
			.bookmarkName("bookmark2")
			.bookmarkLink("link2")
			.category(category)
			.build();
		bookmark2 = bookmarkJPARepository.save(bookmark2);
		BookmarkTag bookmarkTag1 = BookmarkTag.builder()
			.bookmark(bookmark2)
			.tag(tag1)
			.build();
		bookmarkTagJPARepository.save(bookmarkTag1);

		Bookmark bookmark3 = Bookmark.builder()
			.bookmarkName("bookmark3")
			.bookmarkLink("link3")
			.category(category)
			.build();
		bookmark3 = bookmarkJPARepository.save(bookmark3);
		BookmarkTag bookmarkTag2 = BookmarkTag.builder()
			.bookmark(bookmark3)
			.tag(tag1)
			.build();
		bookmarkTagJPARepository.save(bookmarkTag2);
		BookmarkTag bookmarkTag3 = BookmarkTag.builder()
			.bookmark(bookmark3)
			.tag(tag2)
			.build();
		bookmarkTagJPARepository.save(bookmarkTag3);

		// when
		BookmarkSearchCondition searchCondition = BookmarkSearchCondition.builder()
			.tags(List.of("tag1", "tag2"))
			.build();
		Page<Bookmark> result = bookmarkTagJPARepository.search(searchCondition, user.getUserId(),
			PageRequest.of(0, 50));

		// then
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent().get(0).getBookmarkName()).isEqualTo("bookmark3");
	}
}
