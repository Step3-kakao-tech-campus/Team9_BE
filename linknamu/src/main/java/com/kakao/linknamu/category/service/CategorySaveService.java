package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu._core.exception.Exception403;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import com.kakao.linknamu.workspace.service.WorkspaceSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategorySaveService {

    private final CategoryService categoryService;
    // 아직 워크스페이스 서비스에서 findById가 구현되어 있지 않아 레포지토리로 접근했습니다. 이후 수정하겠습니다.
    private final WorkspaceJPARepository workspaceJPARepository;

    public void save(CategorySaveRequestDto requestDto, User user) {
        Workspace workspace = workspaceJPARepository.findById(requestDto.workspaceId()).orElseThrow(
                // throw Exception404
        );

        categoryService.validUser(workspace, user);
        categoryService.findByWorkspaceIdAndCategoryName(requestDto.workspaceId(), requestDto.categoryName()).ifPresent((c) -> {
                throw new Exception400(CategoryExceptionStatus.CATEGORY_ALREADY_EXISTS);
        });

        categoryService.save(requestDto.categoryName(), workspace);
    }

}
