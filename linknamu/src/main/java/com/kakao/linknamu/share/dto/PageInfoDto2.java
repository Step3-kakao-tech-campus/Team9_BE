package com.kakao.linknamu.share.dto;

import org.springframework.data.domain.Page;

public record PageInfoDto2(
	long totalCount,
	int size,
	int currentPage,
	int totalPages
) {

	public PageInfoDto2(Page<?> page) {
		this(
			page.getTotalElements(),
			page.getSize(),
			page.getPageable().getPageNumber(),
			page.getTotalPages()
		);
	}


}
