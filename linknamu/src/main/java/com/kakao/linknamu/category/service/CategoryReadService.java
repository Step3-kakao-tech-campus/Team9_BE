package com.kakao.linknamu.category.service;

import com.kakao.linknamu.category.dto.PageInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CategoryReadService {

    private final CategoryService categoryService;


}
