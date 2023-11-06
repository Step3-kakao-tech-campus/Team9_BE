package com.kakao.linknamu.share.service.category;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkReadService;
import com.kakao.linknamu.bookmarktag.service.BookmarkTagReadService;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.service.CategoryService;
import com.kakao.linknamu.core.dto.PageInfoDto;
import com.kakao.linknamu.core.encryption.AesEncryption;
import com.kakao.linknamu.share.dto.category.GetCategoryFromLinkResponseDto;
import com.kakao.linknamu.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetCategoryFromEncodedIdService {
	private final CategoryService categoryService;
	private final BookmarkReadService bookmarkReadService;
	private final BookmarkTagReadService bookmarkTagReadService;
	private final AesEncryption aesEncryption;

	public GetCategoryFromLinkResponseDto getCategory(String encodedCategoryId, Pageable pageable) {

		String categoryId = aesEncryption.decode(encodedCategoryId);
		Long id = Long.parseLong(categoryId);

		Category category = categoryService.findById(id);

		Page<Bookmark> bookmarkPage = bookmarkReadService.findByCategoryId(id, pageable);

		List<List<Tag>> tagListList = new ArrayList<>();

		for (Bookmark bookmark : bookmarkPage.getContent()) {
			List<Tag> tags = bookmarkTagReadService.findTagByBookmarkId(bookmark.getBookmarkId());
			tagListList.add(tags);
		}

		GetCategoryFromLinkResponseDto responseDto =
			new GetCategoryFromLinkResponseDto(new PageInfoDto(bookmarkPage), category, bookmarkPage.getContent(),
				tagListList);

		return responseDto;

	}
}
