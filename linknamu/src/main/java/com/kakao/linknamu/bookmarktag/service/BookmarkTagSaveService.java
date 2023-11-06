package com.kakao.linknamu.bookmarktag.service;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkReadService;
import com.kakao.linknamu.bookmarktag.dto.CreateBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarktag.repository.BookmarkTagJpaRepository;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.service.TagSaveService;
import com.kakao.linknamu.tag.service.TagSearchService;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkTagSaveService {
	private final BookmarkTagJpaRepository bookmarkTagJPARepository;
	private final BookmarkTagReadService bookmarkTagReadService;
	private final BookmarkReadService bookmarkReadService;
	private final TagSearchService tagSearchService;
	private final TagSaveService tagSaveService;

	public void create(List<BookmarkTag> bookmarkTagList) {
		bookmarkTagJPARepository.saveAll(bookmarkTagList);
	}

	public void create(BookmarkTag bookmarkTag) {
		bookmarkTagJPARepository.save(bookmarkTag);
	}

	public void create(CreateBookmarkTagRequestDto requestDto, User user, Long bookmarkId) {
		Tag tag = tagSearchService.searchByTagNameAndUserId(requestDto.tagName(), user.getUserId())
			.orElseGet(() -> {
				Tag newTag = Tag.builder()
					.tagName(requestDto.tagName())
					.user(user)
					.build();
				tagSaveService.createTag(newTag);
				return newTag;
			});

		Bookmark bookmark = bookmarkReadService.getBookmarkById(bookmarkId);

		bookmarkTagReadService.validDuplicatedBookmarkTag(bookmarkId, tag.getTagId());

		BookmarkTag bookmarkTag = BookmarkTag.builder()
			.bookmark(bookmark)
			.tag(tag)
			.build();

		bookmarkTagJPARepository.save(bookmarkTag);
	}
}
