package com.kakao.linknamu.category.service;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkService;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagService;
import com.kakao.linknamu.category.dto.CategoryGetResponseDto;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.dto.PageInfoDto;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryReadService {

	private final CategoryService categoryService;
	private final BookmarkService bookmarkService;
	private final BookmarkTagService bookmarkTagService;

	public CategoryGetResponseDto getCategory(Long categoryId, User user, Pageable pageable) {
		Category category = categoryService.findByIdFetchJoinWorkspace(categoryId);
		categoryService.validUser(category.getWorkspace(), user);

		Page<Bookmark> bookmarkPage = bookmarkService.findByCategoryId(categoryId, pageable);
		PageInfoDto pageInfoDto = new PageInfoDto(bookmarkPage);
		List<CategoryGetResponseDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
		for (Bookmark bookmark : bookmarkPage.getContent()) {
			List<Tag> tags = bookmarkTagService.findTagByBookmarkId(bookmark.getBookmarkId());
			bookmarkContentDtos.add(CategoryGetResponseDto.BookmarkContentDto.of(bookmark, tags));
		}
		return CategoryGetResponseDto.of(pageInfoDto, bookmarkContentDtos);
	}
}
