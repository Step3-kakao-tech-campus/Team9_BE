package com.kakao.linknamu.tag.service;

import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.TagExceptionStatus;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagSearchService {
	private final TagJpaRepository tagJpaRepository;

	public Optional<Tag> searchByTagNameAndUserId(String name, Long userId) {
		return tagJpaRepository.findByUserIdAndName(userId, name);
	}

	public Optional<Tag> searchByTagName(String name) {
		return tagJpaRepository.findByName(name);
	}

	public String searchTagNameById(Long id) {
		return tagJpaRepository.findNameById(id);
	}

	public List<Long> searchTagIdsByName(String name) {
		return tagJpaRepository.findIdsByName(name);
	}

	public Tag findById(Long id) {
		return tagJpaRepository.findById(id).orElseThrow(
			() -> new Exception404(TagExceptionStatus.TAG_NOT_FOUND)
		);
	}
}
