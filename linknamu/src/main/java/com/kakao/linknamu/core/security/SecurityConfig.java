package com.kakao.linknamu.core.security;

import com.kakao.linknamu.core.exception.Exception401;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.redis.service.BlackListTokenService;
import com.kakao.linknamu.core.util.FilterResponseUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final BlackListTokenService blackListTokenService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
		@Override
		public void configure(HttpSecurity builder) throws Exception {
			AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
			builder.addFilter(new JwtAuthenticationFilter(authenticationManager, blackListTokenService));
			// JwtAuthenticationFilter 전에 JwtExceptionFilter가 적용된다.
			builder.addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);
			super.configure(builder);
		}
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 해제
		http.csrf(CsrfConfigurer::disable);

		// iframe 거부
		http.headers(headersConfigurer ->
			headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
		);

		// cors 재설정
		http.cors(corsConfigurer ->
			corsConfigurer.configurationSource(configurationSource())
		);

		// jSessionId 사용 거부
		http.sessionManagement(sessionManagementConfigurer ->
			sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);

		// form 로그인 해제
		http.formLogin(FormLoginConfigurer::disable);

		// 로그인 인증창 비활성화
		http.httpBasic(HttpBasicConfigurer::disable);

		// 커스텀 필터 적용
		http.apply(new CustomSecurityFilterManager());

		// 인증 실패 처리
		http.exceptionHandling(exceptionHandling ->
			exceptionHandling.authenticationEntryPoint((request, response, authException) ->
				FilterResponseUtils.unAuthorized(response, new Exception401(SecurityExceptionStatus.UNAUTHORIZED)
				))
		);

		// 권한 실패 처리
		http.exceptionHandling(exceptionHandling ->
			exceptionHandling.accessDeniedHandler((request, response, authException) ->
				FilterResponseUtils.forbidden(response, new Exception403(SecurityExceptionStatus.FORBIDDEN)
				))
		);

		// 인증, 권한 필터 설정
		http.authorizeHttpRequests(authorize ->
			authorize.requestMatchers(new AntPathRequestMatcher("/api/auth/google/login"),
					new AntPathRequestMatcher("/api/auth/reissue"),
					new AntPathRequestMatcher("/api/share/workspace/link/**", HttpMethod.GET.name()),
					new AntPathRequestMatcher("/api/share/category/link/**", HttpMethod.GET.name()))
				.permitAll()
				.requestMatchers(
					new AntPathRequestMatcher("/api/bookmark/**"),
					new AntPathRequestMatcher("/api/category/**"),
					new AntPathRequestMatcher("/api/kakao/**"),
					new AntPathRequestMatcher("/api/share/**"),
					new AntPathRequestMatcher("/api/tag/**"),
					new AntPathRequestMatcher("/api/auth/**"),
					new AntPathRequestMatcher("/api/workspace/**"),
					new AntPathRequestMatcher("/api/google-docs/**"))
				.authenticated()
				.anyRequest().permitAll()
		);

		return http.build();
	}

	public CorsConfigurationSource configurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*"); // GET, POST, UPDATE, DELETE 모두 허용
		configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용
		configuration.setAllowCredentials(true); // 클라이언트에게 쿠키 요청 허용
		configuration.addExposedHeader("Authorization"); // 클라이언트에게 Authorization 헤더 접근 허용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
