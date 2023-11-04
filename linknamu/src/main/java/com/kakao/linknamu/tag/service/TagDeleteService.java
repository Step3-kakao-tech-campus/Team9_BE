package com.kakao.linknamu.tag.service;

import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.TagExceptionStatus;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class TagDeleteService {
	private final TagJpaRepository tagJpaRepository;

	public void deleteTagByName(Long userId, String name) {
		Tag tag = tagJpaRepository.findByUserIdAndName(userId, name).orElseThrow(
			() -> new Exception404(TagExceptionStatus.TAG_NOT_FOUND)
		);
		tagJpaRepository.delete(tag);
	}
}
