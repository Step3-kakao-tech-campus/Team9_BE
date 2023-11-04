package com.kakao.linknamu.tag.service;

import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class TagSaveService {
	private final TagJpaRepository tagJpaRepository;

	public void createTag(Tag newTag) {
		tagJpaRepository.save(newTag);
	}
}
