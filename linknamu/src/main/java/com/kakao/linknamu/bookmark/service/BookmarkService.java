package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.*;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarktag.repository.BookmarkTagJpaRepository;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.dto.PageInfoDto;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.service.TagService;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkService {

	private final CategoryService categoryService;
	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final BookmarkTagService bookmarkTagService;
	private final BookmarkTagJpaRepository bookmarkTagJpaRepository;
	private final TagService tagService;

	public Page<Bookmark> findByCategoryId(Long categoryId, Pageable pageable) {
		return bookmarkJpaRepository.findByCategoryId(categoryId, pageable);
	}

	public List<Bookmark> getBookmarkListByCategoryId(Long categoryId) {
		return bookmarkJpaRepository.findListByCategoryId(categoryId);
	}

	public boolean existByBookmarkLinkAndCategoryId(String bookmarkLink, Long categoryId) {
		return bookmarkJpaRepository.findByCategoryIdAndBookmarkLink(categoryId, bookmarkLink).isPresent();
	}

	public void addBookmark(Bookmark bookmark, Category newCategory, List<Tag> tagList, User user) {
		// category는 새로만든 카테고리, 북마크는 과거 북마크 user는 새로운 유저
		Bookmark newBookmark = Bookmark.builder()
			.bookmarkName(bookmark.getBookmarkName())
			.bookmarkDescription(bookmark.getBookmarkDescription())
			.bookmarkThumbnail(bookmark.getBookmarkThumbnail())
			.bookmarkLink(bookmark.getBookmarkLink())
			.category(newCategory)
			.build();

		// Bookmark 테이블에 bookmark 항목 추가
		newCategory = categoryService.findByIdFetchJoinWorkspace(newCategory.getCategoryId());

		if (!newCategory.getWorkspace().getUser().getUserId().equals(user.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}

		// 북마크의 링크에 대한 중복 검사
		bookmarkJpaRepository.findByCategoryIdAndBookmarkLink(newCategory.getCategoryId(),
				newBookmark.getBookmarkLink())
			.ifPresent((b) -> {
				throw new Exception400(BookmarkExceptionStatus.BOOKMARK_ALREADY_EXISTS);
			});

		bookmarkJpaRepository.save(newBookmark);

		// BookmarkTag 테이블에 등록
		List<BookmarkTag> bookmarkTagList = tagList.stream()
			.map(tag -> BookmarkTag.builder()
				.bookmark(newBookmark)
				.tag(tag)
				.build())
			.toList();

		bookmarkTagService.create(bookmarkTagList);
	}

	public void addBookmark(BookmarkRequestDto.BookmarkAddDto bookmarkAddDto, User user) {
		// Bookmark 테이블에 bookmark 항목 추가
		Category category = categoryService.findByIdFetchJoinWorkspace(bookmarkAddDto.getCategoryId());

		if (!category.getWorkspace().getUser().getUserId().equals(user.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}
		// 북마크의 링크에 대한 중복 검사
		bookmarkJpaRepository.findByCategoryIdAndBookmarkLink(category.getCategoryId(),
				bookmarkAddDto.getBookmarkLink())
			.ifPresent((b) -> {
				throw new Exception400(BookmarkExceptionStatus.BOOKMARK_ALREADY_EXISTS);
			});

		Bookmark bookmark = bookmarkAddDto.toEntity(category);

		bookmarkJpaRepository.save(bookmark);

		List<BookmarkTag> bookmarkTagList = new ArrayList<>();
		for (String tagName : bookmarkAddDto.getTags()) {
			// 해당 태그가 존재하지 않는다면 새롭게 생성한다.
			Tag tag = tagService.searchByTagNameAndUserId(tagName, user);
			bookmarkTagList.add(BookmarkTag.builder()
				.bookmark(bookmark)
				.tag(tag)
				.build());
		}

		bookmarkTagService.create(bookmarkTagList);
	}

	public void batchInsertBookmark(List<Bookmark> bookmarkList) {
		bookmarkJpaRepository.batchInsertBookmark(bookmarkList);
	}

	public BookmarkResponseDto.BookmarkGetResponseDto getBookmarkById(Long bookmarkId, User user) {
		BookmarkUserQueryDto bookmark = bookmarkJpaRepository.findByIdBookmarkUserDto(bookmarkId)
			.orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));

		if (!user.getUserId().equals(bookmark.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}

		List<Tag> tagList = bookmarkTagService.findTagByBookmarkId(bookmark.getBookmarkId());

		return BookmarkResponseDto.BookmarkGetResponseDto.of(bookmark, tagList);
	}

	public BookmarkResponseDto.BookmarkUpdateResponseDto updateBookmark(
		BookmarkRequestDto.BookmarkUpdateRequestDto dto,
		Long bookmarkId,
		User user
	) {
		Bookmark bookmark = bookmarkJpaRepository.findByIdFetchJoinCategoryAndWorkspace(bookmarkId)
			.orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));
		bookmarkJpaRepository.updateBookmark(bookmarkId, dto.bookmarkName(), dto.description());
		validUser(bookmark, user);

		List<String> tags = bookmarkTagJpaRepository.findTagNamesByBookmarkId(bookmarkId);

		return BookmarkResponseDto.BookmarkUpdateResponseDto.builder()
			.bookmarkId(bookmarkId)
			.title(bookmark.getBookmarkName())
			.description(bookmark.getBookmarkDescription())
			.url(bookmark.getBookmarkLink())
			.imageUrl(bookmark.getBookmarkThumbnail())
			.tags(tags)
			.build();
	}

	@Transactional
	public void deleteBookmark(Long bookmarkId, User user) {
		Bookmark bookmark = bookmarkJpaRepository.findByIdFetchJoinCategoryAndWorkspace(bookmarkId)
			.orElseThrow(() -> new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND));
		validUser(bookmark, user);
		bookmarkJpaRepository.delete(bookmark);
	}

	@Transactional
	public void moveBookmark(BookmarkRequestDto.BookmarkMoveRequestDto dto, User user) {
		Category toCategory = categoryService.findByIdFetchJoinWorkspace(dto.toCategoryId());

		List<Bookmark> requestedBookmarks = bookmarkJpaRepository.searchRequiredBookmarks(dto.bookmarkIdList());

		Set<Long> examineSet = new HashSet<>();

		for (Bookmark b : requestedBookmarks) {
			validUser(b, user);
			examineSet.add(b.getBookmarkId());
		}

		validExistRequest(examineSet, new HashSet<>(dto.bookmarkIdList()));

		for (Bookmark b : requestedBookmarks) {
			b.moveCategory(toCategory);
		}
	}

	public BookmarkSearchResponseDto searchBookmark(BookmarkSearchCondition condition, User user, Pageable pageable) {
		Page<Bookmark> bookmarks;
		if (isNull(condition.tags())) {
			bookmarks = bookmarkJpaRepository.search(condition, user.getUserId(), pageable);
		} else {
			bookmarks = bookmarkTagJpaRepository.search(condition, user.getUserId(), pageable);
		}
		return BookmarkSearchResponseDto.of(new PageInfoDto(bookmarks), getBookmarkContentDtos(bookmarks));
	}

	private void validUser(Bookmark bookmark, User user) {
		if (!bookmark.getCategory().getWorkspace().getUser().getUserId()
			.equals(user.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}
	}

	// 요청 북마크들이 모두 실제로 디비에 존재하는 북마크인지 체크
	private void validExistRequest(Set<Long> examineSet, Set<Long> requestedSet) {
		requestedSet.removeAll(examineSet);
		if (!requestedSet.isEmpty()) {
			log.error(requestedSet.toString());
			throw new Exception404(BookmarkExceptionStatus.BOOKMARK_NOT_FOUND);
		}
	}

	private List<BookmarkSearchResponseDto.BookmarkContentDto> getBookmarkContentDtos(Page<Bookmark> bookmarks) {
		List<BookmarkSearchResponseDto.BookmarkContentDto> responseDto = new ArrayList<>();
		for (Bookmark bookmark : bookmarks) {
			List<Tag> tags = bookmarkTagJpaRepository.findTagByBookmarkId(bookmark.getBookmarkId());
			responseDto.add(BookmarkSearchResponseDto.BookmarkContentDto.of(bookmark, tags));
		}
		return responseDto;
	}

}
