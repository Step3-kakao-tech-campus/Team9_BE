package com.kakao.linknamu.kakao.controller;

import com.kakao.linknamu._core.util.ApiUtils;
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


    //1. 단순히 file만 요청 받을 경우 => @RequestParam 어노테이션과 MultipartFile 객체사용
    //2, file과 json 을 같이 받을 경우 => @RequestPart 어노테이션과 json을 받을 DTO에 key 값을 지정

    @PostMapping("/send-me")
    public ResponseEntity<?> getKaKaoSendMeText(@RequestPart(value = "file") MultipartFile multipartFile) {

        List<KakaoSendMeResponseDto> responseDtos = kaKaoSendMeExtractService.extractLink(multipartFile);


        return ResponseEntity.ok(ApiUtils.success(responseDtos));
    }
}
