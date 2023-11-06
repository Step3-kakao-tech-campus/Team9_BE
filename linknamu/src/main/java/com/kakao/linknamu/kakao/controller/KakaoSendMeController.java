package com.kakao.linknamu.kakao.controller;

import com.kakao.linknamu.core.util.ApiUtils;
import com.kakao.linknamu.kakao.dto.KakaoSendMeResponseDto;
import com.kakao.linknamu.kakao.service.KaKaoSendMeExtractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoSendMeController {

	private final KaKaoSendMeExtractService kaKaoSendMeExtractService;

	@PostMapping("/send-me")
	public ResponseEntity<?> getKaKaoSendMeText(@RequestPart(value = "file") MultipartFile multipartFile) {

		List<KakaoSendMeResponseDto> responseDtos = kaKaoSendMeExtractService.extractLink(multipartFile);

		return ResponseEntity.ok(ApiUtils.success(responseDtos));
	}
}
