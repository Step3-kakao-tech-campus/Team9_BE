package com.kakao.linknamu.bookmarkTag;

import jakarta.persistence.Embeddable;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkTagId that = (BookmarkTagId) o;
        return Objects.equals(bookmarkId, that.bookmarkId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookmarkId, tagId);
    }

    public BookmarkTagId(Long bookmarkId, Long tagId) {
        this.bookmarkId = bookmarkId;
        this.tagId = tagId;
    }

}
