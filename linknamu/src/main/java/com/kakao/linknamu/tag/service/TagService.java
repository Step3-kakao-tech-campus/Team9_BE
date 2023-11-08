package com.kakao.linknamu.tag.service;

import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.repository.TagJpaRepository;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class TagService {

	private final TagJpaRepository tagJpaRepository;

	public Tag searchByTagNameAndUserId(String name, User user) {
		return tagJpaRepository.findByUserIdAndName(user.getUserId(), name)
			.orElseGet(() -> {
				Tag newTag = Tag.builder()
					.tagName(name)
					.user(user)
					.build();
				tagJpaRepository.save(newTag);
				return newTag;
			});
	}
}
