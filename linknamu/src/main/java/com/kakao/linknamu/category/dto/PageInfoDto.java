package com.kakao.linknamu.category.dto;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

public record PageInfoDto(
        long totalCount,
        int size,
        int currentPage,
        int totalPages
) {

    @Builder
    public PageInfoDto {
    }

    public static PageInfoDto of(Pageable pageable, long totalCount, int totalPages){
        return PageInfoDto.builder()
                .totalCount(totalCount)
                .size(pageable.getPageSize())
                .currentPage(pageable.getPageNumber())
                .totalPages(totalPages)
                .build();
    }
}
