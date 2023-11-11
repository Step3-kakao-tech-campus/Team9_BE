package com.kakao.linknamu.bookmarktag.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class BookmarkTagId implements Serializable {
	private Long bookmarkId;
	private Long tagId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		BookmarkTagId that = (BookmarkTagId)obj;
		return Objects.equals(bookmarkId, that.bookmarkId) && Objects.equals(tagId, that.tagId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookmarkId, tagId);
	}

	@Builder
	public BookmarkTagId(Long bookmarkId, Long tagId) {
		this.bookmarkId = bookmarkId;
		this.tagId = tagId;
	}

}
