package com.kakao.linknamu.kakao.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kakao.linknamu.category.KakaoExceptionStatus;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.kakao.dto.KakaoSendMeResponseDto;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class KaKaoSendMeExtractService {

	public List<KakaoSendMeResponseDto> extractLink(MultipartFile multipartFile) {

		List<KakaoSendMeResponseDto> responseDtos = new ArrayList<>();

		if (multipartFile.isEmpty())
			throw new Exception400(KakaoExceptionStatus.FILE_NOTFOUND);

		String contentType = multipartFile.getContentType();

		boolean isSupportedFormat = (contentType.equals("text/plain")) || (contentType.equals("text/csv"));

		if (isSupportedFormat == false)
			throw new Exception400(KakaoExceptionStatus.FILE_INVALID_FORMAT);

		try {
			byte[] fileBytes = multipartFile.getBytes();
			String fileContent = new String(fileBytes, StandardCharsets.UTF_8); // byte 배열을 문자열로 변환

			//          https 링크를 추출하기 위한 정규 표현식
			String regex = "https?://[a-zA-Z0-9\\-\\.]+(\\:[0-9]+)?\\.[a-zA-Z]{2,3}(\\S*)?";

			//        \bhttps?://\S+   => 모든 http, https 도메인 검출
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(fileContent);
			while (matcher.find()) {
				String httpsLink = matcher.group();
				responseDtos.add(new KakaoSendMeResponseDto(httpsLink));
			}
			return responseDtos;
		} catch (IOException e) {
			throw new Exception500(KakaoExceptionStatus.FILE_READ_FAILED);
		}

	}
}
