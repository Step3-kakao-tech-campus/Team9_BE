package com.kakao.linknamu.user.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.user.UserExceptionStatus;
import com.kakao.linknamu.user.dto.LoginResponseDto;
import com.kakao.linknamu.user.dto.oauth.GoogleUserInfo;
import com.kakao.linknamu.user.service.GoogleService;
import com.kakao.linknamu.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/google")
public class GoogleLoginController {

	private final GoogleService googleService;
	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> googleLogin(HttpServletRequest request) {
		String token = Optional.of(request.getHeader("Google")).orElseThrow(
			() -> new Exception400(UserExceptionStatus.GOOGLE_TOKEN_MISSING));

		GoogleUserInfo userInfo = googleService.getGoogleUserInfo(token);
		LoginResponseDto resultDto = userService.socialLogin(userInfo);
		return ResponseEntity.ok(ApiUtils.success(resultDto));
	}
}
