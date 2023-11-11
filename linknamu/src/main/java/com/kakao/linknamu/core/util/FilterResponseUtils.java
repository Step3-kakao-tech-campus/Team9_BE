package com.kakao.linknamu.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu.core.exception.Exception401;
import com.kakao.linknamu.core.exception.Exception403;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import java.io.IOException;

public class FilterResponseUtils {
	private static final ObjectMapper om = new ObjectMapper();

	public static void unAuthorized(HttpServletResponse response, Exception401 exception) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(exception.status().value());
		response.getWriter().println(om.writeValueAsString(exception.body()));
	}

	public static void forbidden(HttpServletResponse response, Exception403 exception) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(exception.status().value());
		response.getWriter().println(om.writeValueAsString(exception.body()));
	}
}
