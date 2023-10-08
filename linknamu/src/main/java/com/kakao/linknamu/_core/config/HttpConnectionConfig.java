package com.kakao.linknamu._core.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class HttpConnectionConfig {

    // Rest API 요청을 위한 Bean
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(5000))   // 연결시간초과, ms
//                .setReadTimeout(Duration.ofMillis(5000))    // 읽기시간초과, ms
                .build();
    }
}
