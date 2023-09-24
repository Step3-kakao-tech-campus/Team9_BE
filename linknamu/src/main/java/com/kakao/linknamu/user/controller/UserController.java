package com.kakao.linknamu.user.controller;

import com.kakao.linknamu._core.security.CustomUserDetails;
import com.kakao.linknamu._core.security.JwtProvider;
import com.kakao.linknamu._core.util.ApiUtils;
import com.kakao.linknamu.user.dto.ReissueDto;
import com.kakao.linknamu.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody @Valid ReissueDto.ReissueRequestDto requestDto) {
        ReissueDto.ReissueResponseDto resultDto = userService.reissue(requestDto);
        return ResponseEntity.ok(ApiUtils.success(resultDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        userService.logout(request.getHeader(JwtProvider.HEADER));
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.withdrawal(userDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
