package com.kakao.linknamu.bookmark.service;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.bookmark.dto.BookmarkRequestDto;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarktag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagSaveService;
import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJpaRepository;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.core.util.S3ImageClient;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.tag.service.TagSaveService;
import com.kakao.linknamu.tag.service.TagSearchService;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BookmarkCreateService {
	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final CategoryJpaRepository categoryJpaRepository;
	private final TagSearchService tagSearchService;
	private final TagSaveService tagSaveService;
	private final BookmarkTagSaveService bookmarkTagSaveService;
	private final S3ImageClient s3ImageClient;

	public void addBookmark(BookmarkRequestDto.BookmarkAddDto bookmarkAddDto, User userDetails) {

		// Bookmark 테이블에 bookmark 항목 추가
		Category category = categoryJpaRepository.findByIdFetchJoinWorkspace(bookmarkAddDto.getCategoryId())
			.orElseThrow(() -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND));

		if (!category.getWorkspace().getUser().getUserId().equals(userDetails.getUserId())) {
			throw new Exception403(BookmarkExceptionStatus.BOOKMARK_FORBIDDEN);
		}
		// 북마크의 링크에 대한 중복 검사
		bookmarkJpaRepository.findByCategoryIdAndBookmarkLink(category.getCategoryId(),
				bookmarkAddDto.getBookmarkLink())
			.ifPresent((b) -> {
				throw new Exception400(BookmarkExceptionStatus.BOOKMARK_ALREADY_EXISTS);
			});

		String imageUrl = s3ImageClient.base64ImageToS3(bookmarkAddDto.getImageUrl(), bookmarkAddDto.getBookmarkLink());
		Bookmark bookmark = bookmarkAddDto.toEntity(category, imageUrl);

		bookmarkJpaRepository.save(bookmark);


		List<BookmarkTag> bookmarkTagList = new ArrayList<>();
		for (String tagName : bookmarkAddDto.getTags()) {
			// 해당 태그가 존재하지 않는다면 새롭게 생성한다.
			Tag tag = tagSearchService.searchByTagNameAndUserId(tagName, userDetails.getUserId())
				.orElseGet(() -> {
					Tag newTag = Tag.builder()
						.user(userDetails)
						.tagName(tagName)
						.build();
					tagSaveService.createTag(newTag);
					return newTag;
				});
			bookmarkTagList.add(BookmarkTag.builder()
				.bookmark(bookmark)
				.tag(tag)
				.build());
		}

		bookmarkTagSaveService.create(bookmarkTagList);
	}

	public void batchInsertBookmark(List<Bookmark> bookmarkList) {
		bookmarkJpaRepository.batchInsertBookmark(bookmarkList);
	}

	public void addBookmark(Bookmark bookmark, Category newCategory, List<Tag> tagList, User userDetails) {

		// category는 새로만든 카테고리, 북마크는 과거 북마크 user는 새로운 유저
		Bookmark newBookmark = Bookmark.builder()
			.bookmarkName(bookmark.getBookmarkName())
			.bookmarkDescription(bookmark.getBookmarkDescription())
			.bookmarkThumbnail(bookmark.getBookmarkThumbnail())
			.bookmarkLink(bookmark.getBookmarkLink())
			.category(newCategory)
			.build();

		// Bookmark 테이블에 bookmark 항목 추가
		newCategory = categoryJpaRepository.findByIdFetchJoinWorkspace(newCategory.getCategoryId())
			.orElseThrow(() -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND));

		if (!newCategory.getWorkspace().getUser().getUserId().equals(userDetails.getUserId())) {
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

		bookmarkTagSaveService.create(bookmarkTagList);
	}

}
