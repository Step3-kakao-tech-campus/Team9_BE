package com.kakao.linknamu.core.config;

import com.kakao.linknamu.core.filter.LoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}
}
