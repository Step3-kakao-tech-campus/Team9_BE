package com.kakao.linknamu.tag.repository;

import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;
import com.kakao.linknamu.user.repository.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TagJpaRepositoryTest {
	@Autowired
	UserJpaRepository userJPARepository;
	@Autowired
	TagJpaRepository tagJPARepository;

	@Test
	@DisplayName("태그_생성_테스트")
	public void TagSaveTest() {
		// given
		User user = User.builder()
			.email("testemail@pusan.ac.kr")
			.password("testpassword")
			.provider(Provider.PROVIDER_NORMAL)
			.role(Role.ROLE_USER)
			.build();
		user = userJPARepository.save(user);

		Tag tag = Tag.builder()
			.tagName("testTag")
			.user(user)
			.build();

		// when
		Tag savedTag = tagJPARepository.save(tag);

		// then
		assertThat(tag).isEqualTo(savedTag);
		assertThat(tag.getTagId()).isEqualTo(savedTag.getTagId());
		assertThat(tag.getTagName()).isEqualTo(savedTag.getTagName());
		assertThat(tag.getUser().getUserId()).isEqualTo(savedTag.getUser().getUserId());

	}

	@Test
	@DisplayName("북마크-태그_조회_테스트")
	public void BookmarkTagSearchTest() {
		// given
		User user = User.builder()
			.email("testemail@pusan.ac.kr")
			.password("testpassword")
			.provider(Provider.PROVIDER_NORMAL)
			.role(Role.ROLE_USER)
			.build();
		user = userJPARepository.save(user);

		Tag tag = Tag.builder()
			.tagName("testTag")
			.user(user)
			.build();
		tag = tagJPARepository.save(tag);

		// when
		Tag findById = tagJPARepository.findById(tag.getTagId()).orElseThrow();

		// then
		assertThat(findById.getTagName()).isEqualTo(tag.getTagName());
		assertThat(findById.getUser().getUserId()).isEqualTo(tag.getUser().getUserId());
	}
}
