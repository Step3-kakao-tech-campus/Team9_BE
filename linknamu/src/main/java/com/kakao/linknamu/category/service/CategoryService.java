package com.kakao.linknamu.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.bookmark.repository.BookmarkJpaRepository;
import com.kakao.linknamu.bookmarktag.repository.BookmarkTagJpaRepository;
import com.kakao.linknamu.category.dto.CategoryGetResponseDto;
import com.kakao.linknamu.category.dto.CategorySaveRequestDto;
import com.kakao.linknamu.category.dto.CategoryUpdateRequestDto;
import com.kakao.linknamu.core.dto.PageInfoDto;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.tag.entity.Tag;
import com.kakao.linknamu.workspace.service.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kakao.linknamu.category.CategoryExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.category.repository.CategoryJpaRepository;
import com.kakao.linknamu.core.exception.Exception403;
import com.kakao.linknamu.core.exception.Exception404;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.Workspace;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

	private final BookmarkJpaRepository bookmarkJpaRepository;
	private final CategoryJpaRepository categoryJPARepository;
	private final BookmarkTagJpaRepository bookmarkTagJpaRepository;
	private final WorkspaceService workspaceService;

	@Transactional
	public Category save(String categoryName, Workspace workspace) {
		Category category = Category.builder()
			.categoryName(categoryName)
			.workspace(workspace)
			.build();

		validDuplicatedCategoryName(workspace.getId(), categoryName);
		return categoryJPARepository.save(category);
	}

	public Category findByIdFetchJoinWorkspace(Long id) {
		return categoryJPARepository.findByIdFetchJoinWorkspace(id).orElseThrow(
			() -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
		);
	}

	public Category findById(Long id) {
		return categoryJPARepository.findById(id).orElseThrow(
			() -> new Exception404(CategoryExceptionStatus.CATEGORY_NOT_FOUND)
		);
	}

	public Optional<Category> findByWorkspaceIdAndCategoryName(Long workspaceId, String categoryName) {
		return categoryJPARepository.findByWorkspaceIdAndCategoryName(workspaceId, categoryName);
	}

	@Transactional
	public void createCategory(CategorySaveRequestDto requestDto, User user) {
		Workspace workspace = workspaceService.getWorkspaceById(requestDto.workspaceId());
		workspaceService.validUser(workspace, user);
		save(requestDto.categoryName(), workspace);
	}

	public CategoryGetResponseDto getCategory(Long categoryId, User user, Pageable pageable) {
		Category category = findByIdFetchJoinWorkspace(categoryId);
		validUser(category, user);

		Page<Bookmark> bookmarkPage = bookmarkJpaRepository.findByCategoryId(categoryId, pageable);
		PageInfoDto pageInfoDto = new PageInfoDto(bookmarkPage);
		List<CategoryGetResponseDto.BookmarkContentDto> bookmarkContentDtos = new ArrayList<>();
		for (Bookmark bookmark : bookmarkPage.getContent()) {
			List<Tag> tags = bookmarkTagJpaRepository.findTagByBookmarkId(bookmark.getBookmarkId());
			bookmarkContentDtos.add(CategoryGetResponseDto.BookmarkContentDto.of(bookmark, tags));
		}
		return CategoryGetResponseDto.of(pageInfoDto, bookmarkContentDtos);
	}

	@Transactional
	public void update(CategoryUpdateRequestDto requestDto, Long categoryId, User user) {
		Category category = findByIdFetchJoinWorkspace(categoryId);
		validUser(category, user);
		validDuplicatedCategoryName(category.getWorkspace().getId(), requestDto.categoryName());
		category.updateCategoryName(requestDto.categoryName());
	}

	@Transactional
	public void delete(Long categoryId, User user) {
		Category category = findByIdFetchJoinWorkspace(categoryId);
		validUser(category, user);
		categoryJPARepository.deleteById(categoryId);
	}


	public void validUser(Category category, User user) {
		if (!category.getWorkspace().getUser().getUserId().equals(user.getUserId())) {
			throw new Exception403(CategoryExceptionStatus.CATEGORY_FORBIDDEN);
		}
	}

	private void validDuplicatedCategoryName(Long workspaceId, String categoryName) {
		categoryJPARepository.findByWorkspaceIdAndCategoryName(workspaceId, categoryName)
			.ifPresent((c) -> {
				throw new Exception400(CategoryExceptionStatus.CATEGORY_ALREADY_EXISTS);
			});
	}

}
