package com.kakao.linknamu.bookmarktag.entity;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.core.util.AuditingEntity;
import com.kakao.linknamu.tag.entity.Tag;
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
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		BookmarkTag that = (BookmarkTag) obj;
		return Objects.equals(bookmarkTagId, that.bookmarkTagId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookmarkTagId);
	}
}
