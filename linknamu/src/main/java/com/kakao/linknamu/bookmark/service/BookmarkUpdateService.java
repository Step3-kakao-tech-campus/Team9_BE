package com.kakao.linknamu.bookmark.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkUpdateService {
	private final BookmarkJPARepository bookmarkJPARepository;
	private final BookmarkTagSearchService bookmarkTagSearchService;

	@Transactional
	public BookmarkResponseDto.bookmarkUpdateResponseDto bookmarkUpdate(
		BookmarkRequestDto.bookmarkUpdateRequestDto dto,
		Long userId,
		Long bookmarkId
	) {
		bookmarkJPARepository.updateBookmark(bookmarkId, dto.bookmarkName(), dto.description());
		Bookmark bookmark = bookmarkJPARepository.findByIdFetchJoinCategoryAndWorkspace(bookmarkId).orElseThrow(
			() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND)
		);
		if (!bookmark.getCategory().getWorkspace().getUser().getUserId().equals(userId)) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}
		List<String> tags = bookmarkTagSearchService.searchTagNamesByBookmarkId(bookmarkId);
		return BookmarkResponseDto.bookmarkUpdateResponseDto.builder()
			.bookmarkId(bookmarkId)
			.title(bookmark.getBookmarkName())
			.description(bookmark.getBookmarkDescription())
			.url(bookmark.getBookmarkLink())
			.imageUrl(bookmark.getBookmarkThumbnail())
			.tags(tags)
			.build();
	}
}
