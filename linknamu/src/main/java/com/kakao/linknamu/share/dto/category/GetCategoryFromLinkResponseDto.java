package com.kakao.linknamu.share.dto.category;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.share.dto.PageInfoDto2;
import com.kakao.linknamu.tag.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;

public record GetCategoryFromLinkResponseDto(
	PageInfoDto2 pageInfo,
	String categoryName,
	List<BookmarkContentDto> bookmarkContents

) {
	public GetCategoryFromLinkResponseDto(PageInfoDto2 pageInfoDto2, Category category, List<Bookmark> bookmarkList,
										  List<List<Tag>> tagListList) {

		this(pageInfoDto2, category.getCategoryName(), bookmarkList.stream()
			.map(bookmark -> new BookmarkContentDto(bookmark, tagListList.get(bookmarkList.indexOf(bookmark))))
			.collect(Collectors.toList()));

	}

	public record BookmarkContentDto(

		String title,
		String description,
		String url,
		String imageUrl,
		List<TagDto> tags
	) {

		public BookmarkContentDto(Bookmark bookmark, List<Tag> tagList) {
			this(

				bookmark.getBookmarkName(),
				bookmark.getBookmarkDescription(),
				bookmark.getBookmarkLink(),
				bookmark.getBookmarkThumbnail(),
				tagList.stream()
					.map(tag -> new BookmarkContentDto.TagDto(tag.getTagName()))
					.collect(Collectors.toList()));
		}

		private record TagDto(
			String tagName
		) {
		}
	}
}



