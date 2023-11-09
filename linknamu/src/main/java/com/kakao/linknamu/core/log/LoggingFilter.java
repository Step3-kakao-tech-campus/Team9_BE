package com.kakao.linknamu.core.log;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Slf4j
@WebFilter(urlPatterns = "/api/*")
public class LoggingFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);

		chain.doFilter(requestWrapper, response);

		log.info("\n[REQUEST] {} - {} {}\nBody : {}",
			httpRequest.getMethod(),
			httpRequest.getRequestURI(),
			httpResponse.getStatus(),
			getRequestBody(requestWrapper));
	}

	private String getRequestBody(ContentCachingRequestWrapper request) {
		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					return exceptImageUrl(new String(buf, 0, buf.length, wrapper.getCharacterEncoding()));
				} catch (UnsupportedEncodingException e) {
					return " - ";
				}
			}
		}
		return " - ";
	}

	// imageUrl의 형식이 base64인 경우 로그에서 제외한다.
	private String exceptImageUrl(String strJson) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject obj = (JSONObject) jsonParser.parse(strJson);
			if (obj.get("imageUrl") != null) {
				try{
					Base64.getDecoder().decode(obj.get("imageUrl").toString());
					obj.remove("imageUrl");
				} catch (IllegalArgumentException ignored) {}
			}
			return obj.toString();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
