package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.encryption.AesEncryption;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.service.WorkspaceReadService;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import com.kakao.linknamu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class CreateWorkspaceFromEncodedIdService {
	private final WorkspaceService workspaceService;

	private final AesEncryption aesEncryption;
	private final WorkspaceSaveService workspaceSaveService;
	private final CategoryService categoryService;
	private final WorkspaceReadService workspaceReadService;
	private final BookmarkService bookmarkService;
	private final BookmarkTagService bookmarkTagService;

	public void createWorkSpace(String encodedWorkSpaceId, User user) {
		String workspaceId = aesEncryption.decode(encodedWorkSpaceId);
		Long id = Long.parseLong(workspaceId);
		Workspace workspace = workspaceService.getWorkspaceById(id);

		workspaceSaveService.createWorkspace(workspace.getWorkspaceName(), user);
		Optional<Workspace> newWorkspaceOP = workspaceReadService.findWorkspaceByUserAndName(
			workspace.getWorkspaceName(), user);
		Workspace newWorkspace = newWorkspaceOP.orElseThrow(
			() -> new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND)
		);

		for (Category category : workspace.getCategorySet()) {
			Category newCategory = categoryService.save(category.getCategoryName(), newWorkspace);
			List<Bookmark> bookmarkList = bookmarkService.getBookmarkListByCategoryId(category.getCategoryId());
			for (Bookmark bookmark : bookmarkList) {
				List<Tag> tagList = bookmarkTagService.findTagByBookmarkId(bookmark.getBookmarkId());
				bookmarkService.addBookmark(bookmark, newCategory, tagList, user);

			}
		}

	}

}
