package com.kakao.linknamu.bookmark.dto;

import lombok.Builder;

import java.util.List;

public record BookmarkSearchCondition(
        String bookmarkName,
        String bookmarkLink,
        String bookmarkDescription,
        List<String> tags,
        String workspaceName
) {

    @Builder
    public BookmarkSearchCondition{}
}
