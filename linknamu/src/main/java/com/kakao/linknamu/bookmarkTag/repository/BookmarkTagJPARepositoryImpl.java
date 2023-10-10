package com.kakao.linknamu.bookmarkTag.repository;

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

import static com.kakao.linknamu.bookmarkTag.entity.QBookmarkTag.bookmarkTag;
import static com.kakao.linknamu.bookmark.entity.QBookmark.bookmark;
import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class BookmarkTagJPARepositoryImpl implements BookmarkTagJPARepositoryCustom {
    // BookmarkTagJPARepositoryCustom의 search 메서드를 구현한다.

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Bookmark> search(BookmarkSearchCondition condition, Long userId, Pageable pageable) {
        // 기본 검색쿼리를 생성한다.
        JPAQuery<Bookmark> searchQuery = queryFactory
                .select(bookmarkTag.bookmark)
                .from(bookmarkTag)
                .join(bookmarkTag.bookmark, bookmark)
                .where(
                        bookmark.category.workspace.user.userId.eq(userId),
                        bookmarkNameContains(condition.bookmarkName()),
                        bookmarkLinkContains(condition.bookmarkLink()),
                        bookmarkDescriptionContains(condition.bookmarkDescription()),
                        workspaceEq(condition.workspaceName()),
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

    private BooleanExpression bookmarkNameContains(String bookmarkName) {
        return hasText(bookmarkName) ? bookmark.bookmarkName.contains(bookmarkName) : null;
    }

    private BooleanExpression bookmarkLinkContains(String bookmarkLink) {
        return hasText(bookmarkLink) ? bookmark.bookmarkLink.contains(bookmarkLink) : null;
    }

    private BooleanExpression bookmarkDescriptionContains(String bookmarkDescription) {
        return hasText(bookmarkDescription) ? bookmark.bookmarkDescription.contains(bookmarkDescription) : null;
    }

    private BooleanExpression workspaceEq(String workspaceName) {
        return !isNull(workspaceName) ? bookmark.category.workspace.workspaceName.eq(workspaceName) : null;
    }

}
