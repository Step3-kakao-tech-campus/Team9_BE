package com.kakao.linknamu.bookmark.service;

import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.dto.BookmarkSearchResponseDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import com.kakao.linknamu.core.dto.PageInfoDto;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkSearchService {

	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final BookmarkTagSearchService bookmarkTagSearchService;

	public BookmarkSearchResponseDto searchBookmark(BookmarkSearchCondition condition, User user, Pageable pageable) {
		Page<Bookmark> bookmarks;
		if (isNull(condition.tags())) {
			bookmarks = bookmarkJpaRepository.search(condition, user.getUserId(), pageable);
		} else {
			bookmarks = bookmarkTagSearchService.search(condition, user.getUserId(), pageable);
		}
		return BookmarkSearchResponseDto.of(PageInfoDto.of(bookmarks), getBookmarkContentDtos(bookmarks));
	}

	private List<BookmarkSearchResponseDto.BookmarkContentDto> getBookmarkContentDtos(Page<Bookmark> bookmarks) {
		List<BookmarkSearchResponseDto.BookmarkContentDto> responseDto = new ArrayList<>();
		for (Bookmark bookmark : bookmarks) {
			List<Tag> tags = bookmarkTagSearchService.findTagsByBookmarkId(bookmark.getBookmarkId());
			responseDto.add(BookmarkSearchResponseDto.BookmarkContentDto.of(bookmark, tags));
		}
		return responseDto;
	}
}
