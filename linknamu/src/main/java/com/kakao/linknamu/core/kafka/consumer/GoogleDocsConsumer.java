package com.kakao.linknamu.core.kafka.consumer;

import static com.kakao.linknamu.core.util.KafkaTopics.*;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.thirdparty.googledocs.util.GoogleDocsProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GoogleDocsConsumer {
	private final BookmarkService bookmarkService;
	private final ObjectMapper om;
	private final GoogleDocsProvider googleDocsProvider;

	@KafkaListener(topics = {GOOGLE_DOCS_TOPIC}, groupId = "group-id-linknamu")
	public void googleDocsConsumer(String message) throws JsonProcessingException {
		GooglePage googlePage = om.readValue(message, GooglePage.class);
		List<Bookmark> bookmarkList = googleDocsProvider.getLinks(googlePage);
		bookmarkService.batchInsertBookmark(bookmarkList);
	}
}
