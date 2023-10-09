package com.kakao.linknamu.category.service;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.service.BookmarkReadService;
import com.kakao.linknamu.bookmarkTag.service.BookmarkTagReadService;
import com.kakao.linknamu.category.dto.CategoryGetResponseDto;
import com.kakao.linknamu._core.dto.PageInfoDto;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CategoryReadService {

    private final CategoryService categoryService;
    private final BookmarkReadService bookmarkReadService;
    private final BookmarkTagReadService bookmarkTagReadService;


    public CategoryGetResponseDto getCategory(Long categoryId, User user, Pageable pageable) {
        Category category = categoryService.findByIdFetchJoinWorkspace(categoryId);
        categoryService.validUser(category.getWorkspace(), user);

        Page<Bookmark> bookmarkPage = bookmarkReadService.findByCategoryId(categoryId, pageable);
        PageInfoDto pageInfoDto = PageInfoDto.of(bookmarkPage);
        List<CategoryGetResponseDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
        for (Bookmark bookmark : bookmarkPage.getContent()) {
            List<Tag> tags = bookmarkTagReadService.findTagByBookmarkId(bookmark.getBookmarkId());
            bookmarkContentDtos.add(CategoryGetResponseDto.BookmarkContentDto.of(bookmark, tags));
        }
        return CategoryGetResponseDto.of(pageInfoDto, bookmarkContentDtos);
    }
}
