package com.kakao.linknamu.workspace.dto;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.workspace.entity.Workspace;
import lombok.Builder;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record WorkspaceGetResponseDto(
        Long workspaceId,
        String workspaceName,
        List<CategoryResponseDto> categoryList
) {
    record CategoryResponseDto(
            Long categoryId,
            String categoryName
    ){}


    @Builder
    public WorkspaceGetResponseDto {
    }


    public static WorkspaceGetResponseDto of(Workspace workspace) {
        return WorkspaceGetResponseDto.builder()
                .workspaceId(workspace.getId())
                .workspaceName(workspace.getWorkspaceName())
                .categoryList(workspace.getCategoryList().stream()
                        .map((c) -> new CategoryResponseDto(c.getCategoryId(), c.getCategoryName()))
                        .toList())
                .build();
    }
}
