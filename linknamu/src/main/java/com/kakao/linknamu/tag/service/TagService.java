package com.kakao.linknamu.tag.service;

import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJpaRepository;
import com.kakao.linknamu.user.entity.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagService {

	private final TagJpaRepository tagJpaRepository;

	public Optional<Tag> findByTagNameAndUserId(String name, User user) {
		return tagJpaRepository.findByUserIdAndName(user.getUserId(), name);
	}

	@Transactional
	public Tag create(String name, User user) {
		return tagJpaRepository.save(Tag.builder()
			.tagName(name)
			.user(user)
			.build()
		);
	}

}
