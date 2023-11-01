package com.kakao.linknamu.share.service.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.bookmark.service.BookmarkReadService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagReadService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.encryption.AESEncryption;
import com.kakao.linknamu.share.dto.category.CreateCategoryFromEncodedIdRequestDto;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.service.WorkspaceReadService;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import com.kakao.linknamu.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class CreateCategoryFromEncodedIdService {

	private final WorkspaceService workspaceService;

	private final AESEncryption aesEncryption;
	private final WorkspaceSaveService workspaceSaveService;
	private final CategoryService categoryService;
	private final BookmarkReadService bookmarkReadService;
	private final BookmarkCreateService bookmarkCreateService;
	private final WorkspaceReadService workspaceReadService;
	private final BookmarkTagReadService bookmarkTagReadService;

	public void createCategory(String encodedCategoryId, CreateCategoryFromEncodedIdRequestDto requestDto, User user) {
		String categoryId = aesEncryption.decode(encodedCategoryId);
		Long id = Long.parseLong(categoryId);
		Category category = categoryService.findById(id);
		Workspace workspace = workspaceService.getWorkspaceById(requestDto.workSpaceId());
		Category newCategory = categoryService.save(category.getCategoryName(), workspace);
		List<Bookmark> bookmarkList = bookmarkReadService.getBookmarkListByCategoryId(category.getCategoryId());
		for (Bookmark bookmark : bookmarkList) {
			List<Tag> tagList = bookmarkTagReadService.findTagByBookmarkId(bookmark.getBookmarkId());
			bookmarkCreateService.bookmarkAdd(bookmark, newCategory, tagList, user);
		}

	}

}
