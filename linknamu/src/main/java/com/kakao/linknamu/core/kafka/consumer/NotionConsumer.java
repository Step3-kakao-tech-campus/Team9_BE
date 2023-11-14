package com.kakao.linknamu.core.kafka.consumer;

import static com.kakao.linknamu.core.util.KafkaTopics.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.parser.ParserException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.util.JsoupResult;
import com.kakao.linknamu.core.util.JsoupUtils;
import com.kakao.linknamu.thirdparty.notion.dto.NotionKafkaReqeusetDto;
import com.kakao.linknamu.thirdparty.notion.entity.NotionPage;
import com.kakao.linknamu.thirdparty.notion.repository.NotionPageJpaRepository;
import com.kakao.linknamu.thirdparty.notion.util.InvalidNotionApiException;
import com.kakao.linknamu.thirdparty.notion.util.NotionApiUriBuilder;
import com.kakao.linknamu.thirdparty.notion.util.NotionProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotionConsumer {
	private final BookmarkService bookmarkService;
	private final ObjectMapper om;
	private final NotionProvider notionProvider;


	@KafkaListener(topics = {NOTION_TOPIC}, groupId = "group-id-linknamu")
	public void notionConsumer(String message) throws JsonProcessingException {
		NotionKafkaReqeusetDto requsetDto = om.readValue(message, NotionKafkaReqeusetDto.class);

		List<Bookmark> bookmarkList = notionProvider.getPageLinks(
			requsetDto.pageId(),
			requsetDto.accessToken(),
			requsetDto.categoryId()
		);
		bookmarkService.batchInsertBookmark(bookmarkList);
	}
}
