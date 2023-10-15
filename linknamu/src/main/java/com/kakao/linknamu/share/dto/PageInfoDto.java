package com.kakao.linknamu.share.dto;

import org.springframework.data.domain.Page;

public record PageInfoDto(
        long totalCount,
        int size,
        int currentPage,
        int totalPages
) {

    public PageInfoDto(Page<?> page) {
        this(
                page.getTotalElements(),
                page.getSize(),
                page.getPageable().getPageNumber(),
                page.getTotalPages()
        );
    }

   
}
