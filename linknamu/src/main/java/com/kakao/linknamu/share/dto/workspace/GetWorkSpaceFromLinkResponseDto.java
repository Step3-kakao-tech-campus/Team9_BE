package com.kakao.linknamu.share.dto.workspace;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.workspace.entity.Workspace;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record GetWorkSpaceFromLinkResponseDto(
        String workSpaceName,
        List<SharedCategory> sharedCategoryList
) {
    public GetWorkSpaceFromLinkResponseDto(Workspace workspace, List<String> links) {
        this(workspace.getWorkspaceName(), IntStream.range(0, workspace.getCategorySet().size())
                .mapToObj(index -> new SharedCategory(workspace.getCategorySet(), links, index))
                .collect(Collectors.toList())
        );
    }

    public record SharedCategory(
            String categoryName,
            String shareCategoryLink
    ) {
        public SharedCategory(Set<Category> categorySet, List<String> links, int index) {
            this(categorySet.stream().toList().get(index).getCategoryName(),
                    links.get(index)
            );
        }
    }
}