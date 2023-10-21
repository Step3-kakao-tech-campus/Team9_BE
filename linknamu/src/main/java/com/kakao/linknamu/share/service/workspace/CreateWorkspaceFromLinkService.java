package com.kakao.linknamu.share.service.workspace;

import com.kakao.linknamu._core.encryption.AESEncryption;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkCreateService;
import com.kakao.linknamu.bookmark.service.BookmarkReadService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagReadService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
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


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CreateWorkspaceFromLinkService {
    private final WorkspaceService workspaceService;

    private final AESEncryption aesEncryption;
    private final WorkspaceSaveService workspaceSaveService;
    private final CategoryService categoryService;
    private final BookmarkReadService bookmarkReadService;
    private final BookmarkCreateService bookmarkCreateService;
    private final WorkspaceReadService workspaceReadService;
    private final BookmarkTagReadService bookmarkTagReadService;

    public void createWorkSpace(String encodedWorkSpaceId, User user) {
        String workspaceId = aesEncryption.decode(encodedWorkSpaceId);
        Long id = Long.parseLong(workspaceId);
        Workspace workspace = workspaceService.getWorkspaceById(id);


        workspaceSaveService.createWorkspace(workspace.getWorkspaceName(), user);
        Optional<Workspace> newWorkspaceOP = workspaceReadService.findWorkspaceByUserAndName(workspace.getWorkspaceName(), user);
        Workspace newWorkspace = newWorkspaceOP.orElseThrow(
                () -> new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND)
        );

        for (Category category : workspace.getCategorySet()) {
            Category newCategory = categoryService.save(category.getCategoryName(), newWorkspace);
            List<Bookmark> bookmarkList = bookmarkReadService.getBookmarkListByCategoryId(category.getCategoryId());
            for (Bookmark bookmark : bookmarkList) {
                //요기서
                List<Tag> tagList = bookmarkTagReadService.findTagByBookmarkId(bookmark.getBookmarkId());
                bookmarkCreateService.bookmarkAdd(bookmark, newCategory, tagList, user);
                //여기 사이에서 오류가 나는거 같음 북마크 태그만들때 마지막 북마크에 태그리스트가 만들어지지 않음
            }
        }


    }

}
