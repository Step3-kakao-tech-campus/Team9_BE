package com.kakao.linknamu.bookmarktag.repository;

import com.kakao.linknamu.bookmark.dto.BookmarkSearchCondition;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kakao.linknamu.bookmark.entity.QBookmark.bookmark;
import static com.kakao.linknamu.bookmarktag.entity.QBookmarkTag.bookmarkTag;
import static com.kakao.linknamu.tag.entity.QTag.tag;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;


@Repository
@RequiredArgsConstructor
public class BookmarkTagJpaRepositoryImpl implements BookmarkTagJpaRepositoryCustom {


	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Bookmark> search(BookmarkSearchCondition condition, Long userId, Pageable pageable) {
		// 기본 검색쿼리를 생성한다.
		JPAQuery<Bookmark> searchQuery = queryFactory
			.select(bookmarkTag.bookmark)
			.from(bookmarkTag)
			.join(bookmarkTag.bookmark, bookmark)
			.join(bookmarkTag.tag, tag).on(tag.user.userId.eq(userId))
			.where(
				likeBookmarkName(condition.bookmarkName()),
				containsBookmarkLink(condition.bookmarkLink()),
				containsBookmarkDescription(condition.bookmarkDescription()),
				eqWorkspace(condition.workspaceName()),
				bookmarkTag.tag.tagName.in(condition.tags())
			)
			.groupBy(bookmarkTag.bookmark.bookmarkId)
			.having(bookmarkTag.tag.count().eq((long) condition.tags().size()))
			.orderBy(bookmark.createdAt.desc());

		// 페이징을 위해 offset, limit을 검색쿼리에 추가한다.
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

	private BooleanExpression eqWorkspace(String workspaceName) {
		return !isNull(workspaceName) ? bookmark.category.workspace.workspaceName.eq(workspaceName) : null;
	}

}
