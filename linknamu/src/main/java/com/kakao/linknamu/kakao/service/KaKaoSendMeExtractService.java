package com.kakao.linknamu.kakao.service;

import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception500;
import com.kakao.linknamu.kakao.KakaoExceptionStatus;
import com.kakao.linknamu.kakao.dto.KakaoSendMeResponseDto;
import com.kakao.linknamu.thirdparty.utils.JsoupUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class KaKaoSendMeExtractService {

	private final JsoupUtils jsoupUtils;
	private static final String DEFAULT_TITLE = "추출된 링크";

	public List<KakaoSendMeResponseDto> extractLink(MultipartFile multipartFile) {

		List<KakaoSendMeResponseDto> responseDtos = new ArrayList<>();

		if (multipartFile.isEmpty()) {
			throw new Exception400(KakaoExceptionStatus.FILE_NOTFOUND);
		}

		String contentType = multipartFile.getContentType();

		boolean isSupportedFormat = (contentType.equals("text/plain")) || (contentType.equals("text/csv"));

		if (isSupportedFormat == false) {
			throw new Exception400(KakaoExceptionStatus.FILE_INVALID_FORMAT);
		}

		try {
			byte[] fileBytes = multipartFile.getBytes();

			// byte 배열을 문자열로 변환
			String fileContent = new String(fileBytes, StandardCharsets.UTF_8);

			// https 링크를 추출하기 위한 정규 표현식
			String regex = "https?://[a-zA-Z0-9\\-\\.]+(\\:[0-9]+)?\\.[a-zA-Z]{2,3}(\\S*)?";

			// https?://\S+   => 모든 http, https 도메인 검출
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(fileContent);

			// 병렬 쓰레드로 실행
			matcher.results().parallel()
				.forEach(matchResult -> {
					String httpsLink = matchResult.group();
					if (httpsLink.endsWith("\"")) {
						httpsLink = httpsLink.substring(0, httpsLink.length() - 1);
					}
					String title = jsoupUtils.getTitle(httpsLink);
					responseDtos.add(new KakaoSendMeResponseDto(
						title.equals(httpsLink) ? DEFAULT_TITLE : title,
						httpsLink)
					);
				});


			return responseDtos;
		} catch (IOException e) {
			throw new Exception500(KakaoExceptionStatus.FILE_READ_FAILED);
		}

	}
}
