package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu._core.exception.Exception404;
import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.dto.BookmarkResponseDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkUpdateService {
    private final BookmarkJPARepository bookmarkJPARepository;
    private final BookmarkTagSearchService bookmarkTagSearchService;
    public BookmarkResponseDto.bookmarkUpdateResponseDto bookmarkUpdate(
            BookmarkRequestDto.bookmarkUpdateRequestDto dto,
            Long bookmarkId
    ) {
        bookmarkJPARepository.updateBookmark(bookmarkId, dto.bookmarkName(), dto.description());
        Bookmark bookmark = bookmarkJPARepository.findById(bookmarkId).orElseThrow(
                () -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND)
        );
        List<String> tags = bookmarkTagSearchService.searchNamesByBookmarkId(bookmarkId);
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
