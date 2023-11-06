package com.kakao.linknamu.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Profile(value = {"prod"})
@Configuration
public class HttpConnectionProxyConfig {
	private static final String PROXY_HOST = "krmp-proxy.9rum.cc";
	private static final int PROXY_PORT = 3128;


	@Bean
	public RestTemplate restTemplateWithProxy() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
		requestFactory.setProxy(proxy);
		requestFactory.setConnectTimeout(5000); // 연결시간초과, ms
		requestFactory.setReadTimeout(5000); // 읽기시간초과, ms
		return new RestTemplate(requestFactory);
	}
}
