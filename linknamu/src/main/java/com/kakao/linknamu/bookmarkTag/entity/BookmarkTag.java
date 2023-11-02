package com.kakao.linknamu.bookmarkTag.entity;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.core.util.AuditingEntity;
import com.kakao.linknamu.tag.entity.Tag;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bookmark_tag_tb")
public class BookmarkTag extends AuditingEntity {

	@EmbeddedId
	private BookmarkTagId bookmarkTagId;

	@MapsId("bookmarkId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookmark_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Bookmark bookmark;

	@MapsId("tagId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id")
	private Tag tag;

	@Builder
	public BookmarkTag(Bookmark bookmark, Tag tag) {
		this.bookmark = bookmark;
		this.tag = tag;
		this.bookmarkTagId = new BookmarkTagId(bookmark.getBookmarkId(), tag.getTagId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BookmarkTag that = (BookmarkTag)o;
		return Objects.equals(bookmarkTagId, that.bookmarkTagId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookmarkTagId);
	}
}
