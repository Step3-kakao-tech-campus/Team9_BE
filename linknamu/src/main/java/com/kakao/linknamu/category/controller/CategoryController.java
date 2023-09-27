package com.kakao.linknamu.category.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(
            @RequestBody @Valid CategorySaveRequestDto requestDto,
            Errors errors,
            @AuthenticationPrincipal CustomUserDetails user){

        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
