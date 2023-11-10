package com.kakao.linknamu.bookmark.entity;

import com.kakao.linknamu.bookmark.BookmarkExceptionStatus;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.exception.Exception400;
import com.kakao.linknamu.core.util.AuditingEntity;
import com.querydsl.core.annotations.QueryInit;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(
	name = "bookmark_tb",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "category_id bookmark_link unique_constraint",
			columnNames = {"category_id", "bookmark_link"})
	},
	indexes = {
		@Index(
			name = "bookmark_name_index",
			columnList = "bookmark_name")}
)
public class Bookmark extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bookmark_id")
	private Long bookmarkId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@QueryInit("workspace.*")
	private Category category;

	@Column(length = 100, nullable = false, name = "bookmark_name")
	private String bookmarkName;

	@Column(length = 1024, nullable = false, name = "bookmark_link")
	private String bookmarkLink;

	@Column(length = 200, name = "bookmark_description")
	private String bookmarkDescription;

	@Column(length = 1024, name = "bookmark_thumbnail")
	private String bookmarkThumbnail;

	public void moveCategory(Category category) {
		this.category = category;
	}

	@Builder
	public Bookmark(Long bookmarkId, Category category, String bookmarkName, String bookmarkLink,
					String bookmarkDescription, String bookmarkThumbnail) {
		this.bookmarkId = bookmarkId;
		this.category = category;
		this.bookmarkName = bookmarkName;
		this.bookmarkLink = getValidBookmarkLink(bookmarkLink);
		this.bookmarkDescription = bookmarkDescription;
		this.bookmarkThumbnail = bookmarkThumbnail;
	}

	private String getValidBookmarkLink(String bookmarkLink) {
		if(bookmarkLink.length() > 1024) {
			throw new Exception400(BookmarkExceptionStatus.BOOKMARK_LINK_TOO_LONG);
		}
		return bookmarkLink;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Bookmark bookmark = (Bookmark) obj;
		return Objects.equals(getBookmarkId(), bookmark.getBookmarkId())
			&& Objects.equals(getBookmarkLink(), bookmark.getBookmarkLink());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getBookmarkId(), getBookmarkLink());
	}
}
