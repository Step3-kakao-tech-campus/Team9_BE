package com.kakao.linknamu.core.config;

import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class JsonParserConfig {

    @Scope("prototype")
    @Bean
    public JSONParser jsonParser() {
        return new JSONParser();
    }
}
