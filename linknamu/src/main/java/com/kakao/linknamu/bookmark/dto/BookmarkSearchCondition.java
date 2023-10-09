package com.kakao.linknamu.bookmark.dto;

import java.util.List;

public record BookmarkSearchCondition(
        String bookmarkName,
        String bookmarkLink,
        String bookmarkDescription,
        List<String> tags,
        Long workspaceId
) {
}
