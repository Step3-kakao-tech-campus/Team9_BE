package com.kakao.linknamu.workspace.dto;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.workspace.entity.Workspace;

import lombok.Builder;

import java.util.Comparator;
import java.util.List;

public record WorkspaceGetResponseDto(
	Long workspaceId,
	String workspaceName,
	String linkProvider,
	List<CategoryResponseDto> categoryList
) {
	record CategoryResponseDto(
		Long categoryId,
		String categoryName
	) {
	}

	@Builder
	public WorkspaceGetResponseDto {
	}

	public static WorkspaceGetResponseDto of(Workspace workspace) {
		return WorkspaceGetResponseDto.builder()
			.workspaceId(workspace.getId())
			.workspaceName(workspace.getWorkspaceName())
			.linkProvider(workspace.getLinkProvider().name())
			.categoryList(workspace.getCategorySet().stream()
				.sorted(Comparator.comparing(Category::getCategoryId))
				.map((c) -> new CategoryResponseDto(c.getCategoryId(), c.getCategoryName()))
				.toList())
			.build();
	}
}
