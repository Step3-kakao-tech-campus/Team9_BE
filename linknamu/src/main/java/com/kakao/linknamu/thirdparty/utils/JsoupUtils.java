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
	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36";

    public String getTitle(String href) {
        try{
            Connection connection = Jsoup.connect(href)
				.userAgent(USER_AGENT)
				.timeout(5000);

            if(isProxyEnabled) {
                connection = connection.proxy(PROXY_HOST, PROXY_PORT);
            }
			Document doc = connection.get();
			Elements ogTitleTags = doc.select("meta[property=og:title]");
			Elements twitterTitleTags = doc.select("meta[name=twitter:title]");
			if (!ogTitleTags.isEmpty()) {
				return Objects.requireNonNull(ogTitleTags.first()).attr("content");
			}else if(!twitterTitleTags.isEmpty()) {
				return Objects.requireNonNull(twitterTitleTags.first()).attr("content");
			}
            return connection.get().title();
        } catch(IOException e) {
            return href;
        }
    }
}
