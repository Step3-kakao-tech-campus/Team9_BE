package com.kakao.linknamu.bookmark.repository;

import static com.kakao.linknamu.bookmark.entity.QBookmark.*;
import static com.kakao.linknamu.category.entity.QCategory.*;
import static com.kakao.linknamu.workspace.entity.QWorkspace.*;
import static java.util.Objects.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookmarkJpaRepositoryImpl implements BookmarkJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Bookmark> search(BookmarkSearchCondition condition, Long userId, Pageable pageable) {
		JPAQuery<Bookmark> searchQuery = queryFactory
			.select(bookmark)
			.from(bookmark)
			.join(bookmark.category, category)
			.join(category.workspace, workspace).on(workspace.user.userId.eq(userId))
			.where(
				likeBookmarkName(condition.bookmarkName()),
				containsBookmarkLink(condition.bookmarkLink()),
				containsBookmarkDescription(condition.bookmarkDescription()),
				eqWorkspaceName(condition.workspaceName())
			)
			.orderBy(bookmark.createdAt.desc());

		List<Bookmark> bookmarks = searchQuery
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return PageableExecutionUtils.getPage(bookmarks, pageable, searchQuery::fetchCount);
	}

	private BooleanExpression likeBookmarkName(String bookmarkName) {
		return hasText(bookmarkName) ? bookmark.bookmarkName.like(bookmarkName + "%") : null;
	}

	private BooleanExpression containsBookmarkLink(String bookmarkLink) {
		return hasText(bookmarkLink) ? bookmark.bookmarkLink.contains(bookmarkLink) : null;
	}

	private BooleanExpression containsBookmarkDescription(String bookmarkDescription) {
		return hasText(bookmarkDescription) ? bookmark.bookmarkDescription.contains(bookmarkDescription) : null;
	}

	private BooleanExpression eqWorkspaceName(String workspaceName) {
		return !isNull(workspaceName) ? bookmark.category.workspace.workspaceName.eq(workspaceName) : null;
	}
}
