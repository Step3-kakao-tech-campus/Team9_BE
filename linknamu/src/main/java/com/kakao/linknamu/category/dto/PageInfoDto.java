package com.kakao.linknamu.category.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;
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

    public static PageInfoDto of(Page<?> page){
        return PageInfoDto.builder()
                .totalCount(page.getTotalElements())
                .size(page.getSize())
                .currentPage(page.getPageable().getPageNumber())
                .totalPages(page.getTotalPages())
                .build();
    }
}
