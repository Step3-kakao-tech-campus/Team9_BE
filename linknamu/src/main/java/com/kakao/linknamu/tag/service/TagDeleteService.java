package com.kakao.linknamu.tag.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.TagExceptionStatus;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJPARepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class TagDeleteService {
	private final TagJPARepository tagJPARepository;

	public void deleteTagByName(Long userId, String name) {
		Tag tag = tagJPARepository.findByUserIdAndName(userId, name).orElseThrow(
			() -> new Exception404(TagExceptionStatus.TAG_NOT_FOUND)
		);
		tagJPARepository.delete(tag);
	}
}
