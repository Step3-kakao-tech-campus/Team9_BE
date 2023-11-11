package com.kakao.linknamu.core.util;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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
	private static final String DEFAULT_TITLE = "추출된 링크";

	public String getTitle(String href) {
		try {
			Document doc = getDocument(href);
			Elements ogTitleTags = doc.select("meta[property=og:title]");
			Elements twitterTitleTags = doc.select("meta[name=twitter:title]");
			if (!ogTitleTags.isEmpty()) {
				return Objects.requireNonNull(ogTitleTags.first()).attr("content");
			} else if (!twitterTitleTags.isEmpty()) {
				return Objects.requireNonNull(twitterTitleTags.first()).attr("content");
			}
			return doc.title().equals("") ? DEFAULT_TITLE : doc.title();
		} catch (IOException e) {
			return DEFAULT_TITLE;
		}
	}


	public String getImgUrl(String href) {
		try {
			Document doc = getDocument(href);
			// 이 부분은 이미지 URL을 찾는 방법에 따라 다를 수 있습니다.
			// 예를 들어, 이미지 태그를 선택하거나 특정 클래스 또는 속성을 사용할 수 있습니다.
			// 아래는 이미지 태그를 선택하는 예제입니다.
			Elements imgOgTags = doc.select("meta[property=og:image]");
			Elements imgTwitterTags = doc.select("meta[name=twitter:image]");

			if (!imgOgTags.isEmpty()) {
				return getValidImageUrl(imgOgTags.first().attr("content"));
			} else if (!imgTwitterTags.isEmpty()) {
				return getValidImageUrl(imgTwitterTags.first().attr("content"));
			}

			// 이미지를 찾지 못한 경우 기본값
			return DEFAULT_IMAGE;
		} catch (IOException e) {
			//연결 실패시 기본값
			return DEFAULT_IMAGE;

		}
	}

	// Jsoup 요청 후 image, title모두 받아오는 메소드
	public JsoupResult getTitleAndImgUrl(String href) {
		try {
			Document doc = getDocument(href);
			Elements ogTitleTags = doc.select("meta[property=og:title]");
			Elements twitterTitleTags = doc.select("meta[name=twitter:title]");
			Elements imgOgTags = doc.select("meta[property=og:image]");
			Elements imgTwitterTags = doc.select("meta[name=twitter:image]");

			String title;
			String imageUrl = DEFAULT_IMAGE;

			if (!ogTitleTags.isEmpty()) {
				title = ogTitleTags.first().attr("content");
			} else if (!twitterTitleTags.isEmpty()) {
				title = twitterTitleTags.first().attr("content");
			} else {
				title = doc.title();
			}

			if (!imgOgTags.isEmpty()) {
				imageUrl = getValidImageUrl(imgOgTags.first().attr("content"));
			} else if (!imgTwitterTags.isEmpty()) {
				imageUrl = getValidImageUrl(imgTwitterTags.first().attr("content"));
			}

			// 이미지 혹은 제목을 찾지 못한 경우 기본값
			return new JsoupResult(title.equals("") ? DEFAULT_TITLE : title, imageUrl);
		} catch (IOException e) {
			// 연결 실패시 기본값
			return new JsoupResult(DEFAULT_TITLE, DEFAULT_IMAGE);
		}
	}

	private Document getDocument(String href) throws IOException{
		Connection connection = Jsoup.connect(href)
			.userAgent(USER_AGENT)
			.timeout(5000);

		if (isProxyEnabled) {
			connection = connection.proxy(PROXY_HOST, PROXY_PORT);
		}

		return connection.get();
	}

	private String getValidImageUrl(String imgUrlString) {
		try {
			URL imgUrl = new URL(imgUrlString);
			BufferedImage image = ImageIO.read(imgUrl);

			// image인지 체크하는 로직
			if (image == null) {
				return DEFAULT_IMAGE;
			}
		} catch (IOException exception) {
			return DEFAULT_IMAGE;
		}

		return imgUrlString;
	}
}
