package com.kakao.linknamu.bookmarktag.service;

import com.kakao.linknamu.bookmarktag.BookmarkTagExceptionStatus;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTagId;
import com.kakao.linknamu.bookmarktag.repository.BookmarkTagJpaRepository;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookmarkTagReadService {

	private final BookmarkTagJpaRepository bookmarkTagJPARepository;

	public List<Tag> findTagByBookmarkId(Long bookmarkId) {
		return bookmarkTagJPARepository.findTagByBookmarkId(bookmarkId);
	}

	public BookmarkTag findByIdFetchJoinTag(Long bookmarkId, Long tagId) {
		BookmarkTagId bookmarkTagId = BookmarkTagId.builder()
			.bookmarkId(bookmarkId)
			.tagId(tagId)
			.build();

		return bookmarkTagJPARepository.findByIdFetchJoinTag(bookmarkTagId)
			.orElseThrow(() -> new Exception404(BookmarkTagExceptionStatus.BOOKMARK_TAG_NOT_FOUND));
	}

	public void validDuplicatedBookmarkTag(Long bookmarkId, Long tagId) {
		BookmarkTagId bookmarkTagId = BookmarkTagId.builder()
			.bookmarkId(bookmarkId)
			.tagId(tagId)
			.build();

		if (bookmarkTagJPARepository.existsById(bookmarkTagId)) {
			throw new Exception400(BookmarkTagExceptionStatus.BOOKMARK_TAG_DUPLICATE);
		}
	}
}
