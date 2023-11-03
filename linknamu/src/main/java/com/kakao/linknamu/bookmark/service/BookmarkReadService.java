package com.kakao.linknamu.bookmark.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.dto.BookmarkUserQueryDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagReadService;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookmarkReadService {

	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final BookmarkTagReadService bookmarkTagReadService;

	public Page<Bookmark> findByCategoryId(Long categoryId, Pageable pageable) {
		return bookmarkJpaRepository.findByCategoryId(categoryId, pageable);
	}

	public List<Bookmark> getBookmarkListByCategoryId(Long categoryId) {
		return bookmarkJpaRepository.findListByCategoryId(categoryId);
	}

	public Bookmark getBookmarkById(Long bookmarkId) {
		return bookmarkJpaRepository.findById(bookmarkId)
			.orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));
	}

	public BookmarkResponseDto.BookmarkGetResponseDto getBookmarkById(Long bookmarkId, User user) {

		BookmarkUserQueryDto bookmark = bookmarkJpaRepository.findByIdBookmarkUserDto(bookmarkId)
			.orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));

		if (!user.getUserId().equals(bookmark.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}

		List<Tag> tagList = bookmarkTagReadService.findTagByBookmarkId(bookmark.getBookmarkId());

		return BookmarkResponseDto.BookmarkGetResponseDto.of(bookmark, tagList);
	}
}
