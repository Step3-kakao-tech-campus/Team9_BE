package com.kakao.linknamu.category.service;

import com.kakao.linknamu._core.exception.Exception400;
import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.WorkspaceExceptionStatus;
import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.repository.WorkspaceJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategorySaveService {

    private final CategoryService categoryService;
    private final WorkspaceJPARepository workspaceJPARepository;

    public void save(CategorySaveRequestDto requestDto, User user) {
        Workspace workspace = workspaceJPARepository.findById(requestDto.workspaceId()).orElseThrow(() ->
                new Exception404(WorkspaceExceptionStatus.WORKSPACE_NOT_FOUND)
        );

        categoryService.validUser(workspace, user);
        categoryService.findByWorkspaceIdAndCategoryName(requestDto.workspaceId(), requestDto.categoryName()).ifPresent((c) -> {
                throw new Exception400(CategoryExceptionStatus.CATEGORY_ALREADY_EXISTS);
        });

        categoryService.save(requestDto.categoryName(), workspace);
    }

}
