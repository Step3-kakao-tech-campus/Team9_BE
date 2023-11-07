package com.kakao.linknamu.thirdparty.notion.util;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotionApiUriBuilder {
	private static final String BLOCK_URI = "https://api.notion.com/v1/blocks/";
	private static final int PAGE_SIZE = 100;


	public String getBlockUri(String pageId, Optional<String> nextCursor) {
		StringBuilder sb = new StringBuilder();
		sb.append(BLOCK_URI)
			.append(pageId)
			.append("/children?")
			.append(String.format("page_size=%d", PAGE_SIZE));

		nextCursor.ifPresent(s -> sb.append(String.format("start_cursor=%s", s)));

		return sb.toString();
	}
}
