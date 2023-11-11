package com.kakao.linknamu.share.service.category;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagService;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.dto.PageInfoDto;
import com.kakao.linknamu.core.encryption.AesEncryption;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.share.dto.category.CreateCategoryFromEncodedIdRequestDto;
import com.kakao.linknamu.share.dto.category.GetCategoryFromLinkResponseDto;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ShareCategoryService {

	private final AesEncryption aesEncryption;
	private final BookmarkService bookmarkService;
	private final BookmarkTagService bookmarkTagService;
	private final CategoryService categoryService;
	private final WorkspaceService workspaceService;
	private static final String URL = "/share-link/category/share?category=";

	public String createLink(Long categoryId, User user) {
		Category category = categoryService.findByIdFetchJoinWorkspace(categoryId);
		categoryService.validUser(category, user);
		String encodedString = aesEncryption.encode(categoryId.toString());
		return URL + encodedString;
	}

	public GetCategoryFromLinkResponseDto getCategory(String encodedCategoryId, Pageable pageable) {
		Category category = getCategoryByEncodedCategoryId(encodedCategoryId);
		Page<Bookmark> bookmarkPage = bookmarkService.findByCategoryId(category.getCategoryId(), pageable);

		List<List<Tag>> tagListList = new ArrayList<>();
		for (Bookmark bookmark : bookmarkPage.getContent()) {
			List<Tag> tags = bookmarkTagService.findTagByBookmarkId(bookmark.getBookmarkId());
			tagListList.add(tags);
		}
		return new GetCategoryFromLinkResponseDto(new PageInfoDto(bookmarkPage), category, bookmarkPage.getContent(),
			tagListList);
	}

	@Transactional
	public void createCategory(String encodedCategoryId, CreateCategoryFromEncodedIdRequestDto requestDto, User user) {
		Category category = getCategoryByEncodedCategoryId(encodedCategoryId);
		Workspace workspace = workspaceService.getWorkspaceById(requestDto.workSpaceId());
		Category newCategory = categoryService.save(category.getCategoryName(), workspace);

		List<Bookmark> bookmarkList = bookmarkService.getBookmarkListByCategoryId(category.getCategoryId());
		for (Bookmark bookmark : bookmarkList) {
			List<Tag> tagList = bookmarkTagService.findTagByBookmarkId(bookmark.getBookmarkId());
			bookmarkService.addBookmark(bookmark, newCategory, tagList, user);
		}
	}

	private Category getCategoryByEncodedCategoryId(String encodedCategoryId) {
		String categoryId = aesEncryption.decode(encodedCategoryId);
		Long id = Long.parseLong(categoryId);
		return categoryService.findById(id);
	}
}
