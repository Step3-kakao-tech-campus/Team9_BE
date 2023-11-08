package com.kakao.linknamu.thirdparty.utils;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class JsoupUtils {

	@Value("${proxy.enabled:false}")
	private boolean isProxyEnabled;

	private static final String PROXY_HOST = "krmp-proxy.9rum.cc";
	private static final int PROXY_PORT = 3128;
	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
		+ "AppleWebKit/537.36 (KHTML, like Gecko) "
		+ "Chrome/90.0.4430.93 Safari/537.36";


	private static final String DEFAULT_IMAGE = "https://linknamu-image.s3.ap-northeast-2.amazonaws.com/image/default_image.png";

	public String getTitle(String href) {
		try {
			Connection connection = Jsoup.connect(href)
				.userAgent(USER_AGENT)
				.timeout(5000);

			if (isProxyEnabled) {
				connection = connection.proxy(PROXY_HOST, PROXY_PORT);
			}
			Document doc = connection.get();
			Elements ogTitleTags = doc.select("meta[property=og:title]");
			Elements twitterTitleTags = doc.select("meta[name=twitter:title]");
			if (!ogTitleTags.isEmpty()) {
				return Objects.requireNonNull(ogTitleTags.first()).attr("content");
			} else if (!twitterTitleTags.isEmpty()) {
				return Objects.requireNonNull(twitterTitleTags.first()).attr("content");
			}
			return connection.get().title();
		} catch (IOException e) {
			return href;
		}
	}


	public String getImgUrl(String href) {
		try {
			Connection connection = Jsoup.connect(href)
				.userAgent(USER_AGENT)
				.timeout(5000);

			if (isProxyEnabled) {
				connection = connection.proxy(PROXY_HOST, PROXY_PORT);
			}
			Document doc = connection.get();
			// 이 부분은 이미지 URL을 찾는 방법에 따라 다를 수 있습니다.
			// 예를 들어, 이미지 태그를 선택하거나 특정 클래스 또는 속성을 사용할 수 있습니다.
			// 아래는 이미지 태그를 선택하는 예제입니다.
			Elements imgTags = doc.select("img");


			if (!imgTags.isEmpty()) {
				// 첫 번째 이미지 태그의 'src' 속성을 가져옵니다.
				String imageUrl = imgTags.first().attr("src");
				System.out.println(imageUrl);
				return imageUrl;
			}
			// 이미지를 찾지 못한 경우 기본값
			return DEFAULT_IMAGE;
		} catch (IOException e) {
			//연결 실패시 기본값
			return DEFAULT_IMAGE;

		}
	}
}
