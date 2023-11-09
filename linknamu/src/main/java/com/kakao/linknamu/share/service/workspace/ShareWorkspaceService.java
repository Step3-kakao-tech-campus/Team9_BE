package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.encryption.AesEncryption;
import com.kakao.linknamu.share.dto.workspace.GetWorkSpaceFromLinkResponseDto;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ShareWorkspaceService {

	private final AesEncryption aesEncryption;
	private final WorkspaceService workspaceService;
	private final CategoryService categoryService;
	private final BookmarkTagService bookmarkTagService;
	private final BookmarkService bookmarkService;
	private static final String URL = "/share-link/workspace/share?workspace=";

	public String createLink(Long workSpaceId) {
		workspaceService.getWorkspaceById(workSpaceId);
		String encodedString = aesEncryption.encode(workSpaceId.toString());
		return URL + encodedString;
	}

	public GetWorkSpaceFromLinkResponseDto getWorkspace(String encodedId) {
		Workspace workspace = getWorkspaceByEncodedWorkspaceId(encodedId);

		List<String> links = new ArrayList<>();
		for (Category category : workspace.getCategorySet()) {
			String encodedCategoryId = aesEncryption.encode(category.getCategoryId().toString());
			links.add(encodedCategoryId);
		}
		return new GetWorkSpaceFromLinkResponseDto(workspace, links);
	}

	@Transactional
	public void createWorkSpace(String encodedWorkspaceId, User user) {
		Workspace workspace = getWorkspaceByEncodedWorkspaceId(encodedWorkspaceId);
		Workspace newWorkspace = workspaceService.createWorkspace(workspace.getWorkspaceName(), user);

		for (Category category : workspace.getCategorySet()) {
			Category newCategory = categoryService.save(category.getCategoryName(), newWorkspace);
			List<Bookmark> bookmarkList = bookmarkService.getBookmarkListByCategoryId(category.getCategoryId());
			for (Bookmark bookmark : bookmarkList) {
				List<Tag> tagList = bookmarkTagService.findTagByBookmarkId(bookmark.getBookmarkId());
				bookmarkService.addBookmark(bookmark, newCategory, tagList, user);
			}
		}
	}

	private Workspace getWorkspaceByEncodedWorkspaceId(String encodedWorkspaceId) {
		String workspaceId = aesEncryption.decode(encodedWorkspaceId);
		Long id = Long.parseLong(workspaceId);
		return workspaceService.getWorkspaceById(id);
	}

}
