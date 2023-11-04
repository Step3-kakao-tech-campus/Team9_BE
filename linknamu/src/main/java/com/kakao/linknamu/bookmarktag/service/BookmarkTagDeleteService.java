package com.kakao.linknamu.bookmarktag.service;

import com.kakao.linknamu.bookmarktag.BookmarkTagExceptionStatus;
import com.kakao.linknamu.bookmarktag.dto.DeleteBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTagId;
import com.kakao.linknamu.bookmarktag.repository.BookmarkTagJpaRepository;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkTagDeleteService {
	private final BookmarkTagJpaRepository bookmarkTagJPARepository;

	public void deleteBookmarkTag(Long bookmarkId, String tagName) {
		BookmarkTag bookmarkTag = bookmarkTagJPARepository.findMatchingBookmarkTag(bookmarkId, tagName);
		bookmarkTagJPARepository.delete(bookmarkTag);
	}

	public void deleteByTagIdAndBookmarkId(DeleteBookmarkTagRequestDto requestDto, User user) {
		BookmarkTagId bookmarkTagId = BookmarkTagId.builder()
			.bookmarkId(requestDto.bookmarkId())
			.tagId(requestDto.tagId())
			.build();

		BookmarkTag bookmarkTag = bookmarkTagJPARepository.findByIdFetchJoinTag(bookmarkTagId)
			.orElseThrow(() -> new Exception404(BookmarkTagExceptionStatus.BOOKMARK_TAG_NOT_FOUND));

		validWriter(bookmarkTag.getTag().getUser().getUserId(), user.getUserId());

		bookmarkTagJPARepository.delete(bookmarkTag);
	}

	public void deleteByEntity(BookmarkTag bookmarkTag) {
		bookmarkTagJPARepository.delete(bookmarkTag);
	}

	private void validWriter(Long writerId, Long requestUserId) {
		if (!writerId.equals(requestUserId))
			throw new Exception403(BookmarkTagExceptionStatus.BOOKMARK_TAG_FORBIDDEN);
	}
}
