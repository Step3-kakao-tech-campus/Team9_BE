package com.kakao.linknamu.bookmark.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJPARepository;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagSaveService;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJPARepository;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.service.TagSaveService;
import com.kakao.linknamu.tag.service.TagSearchService;
import com.kakao.linknamu.user.entity.User;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkCreateService {
	private final BookmarkJPARepository bookmarkJPARepository;
	private final CategoryJPARepository categoryJPARepository;
	private final TagSearchService tagSearchService;
	private final TagSaveService tagSaveService;
	private final BookmarkTagSaveService bookmarkTagSaveService;

	public void bookmarkAdd(BookmarkRequestDto.BookmarkAddDTO bookmarkAddDTO, User userDetails) {
		/* Bookmark 테이블에 bookmark 항목 추가 */
		Category category = categoryJPARepository.findByIdFetchJoinWorkspace(bookmarkAddDTO.getCategoryId())
			.orElseThrow(
				() -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
			);
		if (!category.getWorkspace().getUser().getUserId().equals(userDetails.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}
		// 북마크의 링크에 대한 중복 검사
		bookmarkJPARepository.findByCategoryIdAndBookmarkLink(category.getCategoryId(),
			bookmarkAddDTO.getBookmarkLink()).ifPresent((b) -> {
			throw new Exception400(BookmarkExceptionStatus.BOOKMARK_ALREADY_EXISTS);
		});

		Bookmark bookmark = bookmarkAddDTO.toBookmarkEntity(category);
		bookmarkJPARepository.save(bookmark);

		/* 새로운 Tag일 경우 Tag 테이블에 등록해야 한다. */
		List<Tag> tagEntities = new ArrayList<>();
		for (String tagName : bookmarkAddDTO.getTags()) {
			// 해당 태그가 존재하지 않는다면 새롭게 생성해야 한다.
			// 생성된 태그는 태그 테이블에 등록되어야 한다.
			Tag tag = tagSearchService.searchByTagNameAndUserId(tagName, userDetails.getUserId())
				.orElseGet(() -> {
					Tag newTag = Tag.builder()
						.user(userDetails)
						.tagName(tagName)
						.build();
					tagSaveService.createTag(newTag);
					return newTag;
				});
			tagEntities.add(tag);
		}

		/* BookmarkTag 테이블에 등록 */
		List<BookmarkTag> bookmarkTagList = tagEntities.stream()
			.map(tag -> BookmarkTag.builder()
				.bookmark(bookmark)
				.tag(tag)
				.build())
			.toList();
		bookmarkTagSaveService.createPairs(bookmarkTagList);
	}

	public void bookmarkBatchInsert(List<Bookmark> bookmarkList) {
		bookmarkJPARepository.bookmarkBatchInsert(bookmarkList);
	}

	public void bookmarkAdd(Bookmark bookmark, Category newCategory, List<Tag> tagList, User userDetails) {

		//category는 새로만든 카테고리, 북마크는 과거 북마크 user는 새로운 유저
		Bookmark newBookmark = Bookmark.builder()
			.bookmarkName(bookmark.getBookmarkName())
			.bookmarkDescription(bookmark.getBookmarkDescription())
			.bookmarkThumbnail(bookmark.getBookmarkThumbnail())
			.bookmarkLink(bookmark.getBookmarkLink())
			.category(newCategory).build();
		/* Bookmark 테이블에 bookmark 항목 추가 */
		newCategory = categoryJPARepository.findByIdFetchJoinWorkspace(newCategory.getCategoryId()).orElseThrow(
			() -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
		);
		if (!newCategory.getWorkspace().getUser().getUserId().equals(userDetails.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}
		// 북마크의 링크에 대한 중복 검사
		bookmarkJPARepository.findByCategoryIdAndBookmarkLink(newCategory.getCategoryId(),
			newBookmark.getBookmarkLink()).ifPresent((b) -> {
			throw new Exception400(BookmarkExceptionStatus.BOOKMARK_ALREADY_EXISTS);
		});

		bookmarkJPARepository.save(newBookmark);

		/* 새로운 Tag일 경우 Tag 테이블에 등록해야 한다. */
		//새로운 태그일수가없다. 이미 존재하는 태그를 가져오므로
		//대신 그 태그를 북마크와 연결시켜줘야 한다. => 북마크-태크 서비스 가져오자!
		/* BookmarkTag 테이블에 등록 */
		List<BookmarkTag> bookmarkTagList = tagList.stream()
			.map(tag -> BookmarkTag.builder()
				.bookmark(newBookmark)
				.tag(tag)
				.build())
			.toList();

		bookmarkTagSaveService.createPairs(bookmarkTagList);
	}

}
