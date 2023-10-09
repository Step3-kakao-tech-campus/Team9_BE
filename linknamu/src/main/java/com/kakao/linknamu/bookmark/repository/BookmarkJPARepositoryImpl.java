package com.kakao.linknamu.bookmark.repository;

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
import static com.kakao.linknamu.bookmarkTag.entity.QBookmarkTag.bookmarkTag;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class BookmarkJPARepositoryImpl implements BookmarkJPARepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Bookmark> search(BookmarkSearchCondition condition, Long userId, Pageable pageable) {
        JPAQuery<Bookmark> searchQuery = queryFactory
                .select(bookmark)
                .from(bookmark)
                .where(
                        bookmark.category.workspace.user.userId.eq(userId),
                        bookmarkNameContains(condition.bookmarkName()),
                        bookmarkLinkContains(condition.bookmarkLink()),
                        bookmarkDescriptionContains(condition.bookmarkDescription()),
                        workspaceEq(condition.workspaceId())
                )
                .orderBy(bookmark.createdAt.desc());

        List<Bookmark> bookmarks = searchQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(bookmarks, pageable, searchQuery::fetchCount);
    }

    private BooleanExpression bookmarkNameContains(String bookmarkName) {
        return hasText(bookmarkName) ? bookmark.bookmarkName.contains(bookmarkName) : null;
    }

    private BooleanExpression bookmarkLinkContains(String bookmarkLink) {
        return hasText(bookmarkLink) ? bookmark.bookmarkLink.contains(bookmarkLink) : null;
    }

    private BooleanExpression bookmarkDescriptionContains(String bookmarkDescription) {
        return hasText(bookmarkDescription) ? bookmark.bookmarkDescription.contains(bookmarkDescription) : null;
    }

    private BooleanExpression workspaceEq(Long workspaceId) {
        return !isNull(workspaceId) ? bookmark.category.workspace.id.eq(workspaceId) : null;
    }
}
