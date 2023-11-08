package com.kakao.linknamu.bookmarktag.service;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarktag.BookmarkTagExceptionStatus;
import com.kakao.linknamu.bookmarktag.dto.CreateBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarktag.dto.DeleteBookmarkTagRequestDto;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTagId;
import com.kakao.linknamu.bookmarktag.repository.BookmarkTagJpaRepository;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.service.TagService;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkTagService {

	private final BookmarkTagJpaRepository bookmarkTagJPARepository;
	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final TagService tagService;

	public void create(CreateBookmarkTagRequestDto requestDto, User user, Long bookmarkId) {
		Tag tag = tagService.searchByTagNameAndUserId(requestDto.tagName(), user);

		Bookmark bookmark = bookmarkJpaRepository.findById(bookmarkId).orElseThrow(() ->
			new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));

		validDuplicatedBookmarkTag(bookmarkId, tag.getTagId());

		BookmarkTag bookmarkTag = BookmarkTag.builder()
			.bookmark(bookmark)
			.tag(tag)
			.build();

		bookmarkTagJPARepository.save(bookmarkTag);
	}

	public List<Tag> findTagByBookmarkId(Long bookmarkId) {
		return bookmarkTagJPARepository.findTagByBookmarkId(bookmarkId);
	}

	public void deleteByTagIdAndBookmarkId(DeleteBookmarkTagRequestDto requestDto, User user) {
		BookmarkTagId bookmarkTagId = BookmarkTagId.builder()
			.bookmarkId(requestDto.bookmarkId())
			.tagId(requestDto.tagId())
			.build();

		BookmarkTag bookmarkTag = bookmarkTagJPARepository.findByIdFetchJoinTag(bookmarkTagId)
			.orElseThrow(() -> new Exception404(BookmarkTagExceptionStatus.BOOKMARK_TAG_NOT_FOUND));

		validUser(bookmarkTag, user);

		bookmarkTagJPARepository.delete(bookmarkTag);
	}

	private void validDuplicatedBookmarkTag(Long bookmarkId, Long tagId) {
		BookmarkTagId bookmarkTagId = BookmarkTagId.builder()
			.bookmarkId(bookmarkId)
			.tagId(tagId)
			.build();

		if (bookmarkTagJPARepository.existsById(bookmarkTagId)) {
			throw new Exception400(BookmarkTagExceptionStatus.BOOKMARK_TAG_DUPLICATE);
		}
	}

	private void validUser(BookmarkTag bookmarkTag, User user) {
		if (!bookmarkTag.getTag().getUser().getUserId().equals(user.getUserId()))
			throw new Exception403(BookmarkTagExceptionStatus.BOOKMARK_TAG_FORBIDDEN);
	}
}
