package com.kakao.linknamu._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kakao.linknamu.core.filter.LoggingFilter;

@Configuration
public class FilterConfig {
	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}
}
