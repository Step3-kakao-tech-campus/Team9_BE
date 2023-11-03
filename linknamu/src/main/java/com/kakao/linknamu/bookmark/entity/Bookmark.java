package com.kakao.linknamu.bookmark.entity;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.util.AuditingEntity;
import com.querydsl.core.annotations.QueryInit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
	name = "bookmark_tb",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "categoryId_bookmarkLink unique constraint",
			columnNames = {
				"category_id",
				"bookmark_link"
			}
		)
	},
	indexes = @Index(name = "idx__bookmark_name", columnList = "bookmark_name")
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

	@Column(length = 512, name = "bookmark_thumbnail")
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
		this.bookmarkLink = bookmarkLink;
		this.bookmarkDescription = bookmarkDescription;
		this.bookmarkThumbnail = bookmarkThumbnail;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(getBookmarkId(), bookmark.getBookmarkId()) && Objects.equals(getBookmarkLink(), bookmark.getBookmarkLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookmarkId(), getBookmarkLink());
    }
}
