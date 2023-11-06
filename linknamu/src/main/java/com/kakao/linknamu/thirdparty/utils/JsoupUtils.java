package com.kakao.linknamu.thirdparty.utils;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JsoupUtils {

	@Value("${proxy.enabled:false}")
	private boolean isProxyEnabled;


	private static final String PROXY_HOST = "krmp-proxy.9rum.cc";
	private static final int PROXY_PORT = 3128;

	public String getTitle(String href) {
		try {
			Connection connection = Jsoup.connect(href)
				.timeout(5000);

			if (isProxyEnabled) {
				connection = connection.proxy(PROXY_HOST, PROXY_PORT);
			}

			return connection.get().title();
		} catch (IOException e) {
			return href;
		}
	}
}
