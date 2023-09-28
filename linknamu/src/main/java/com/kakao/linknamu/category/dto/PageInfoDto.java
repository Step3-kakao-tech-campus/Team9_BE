package com.kakao.linknamu.category.dto;

public record PageInfoDto(
        int totalCount,
        int size,
        int currentPage,
        int totalPages
) {
}
