package com.kakao.linknamu.bookmarkTag.entity;

import com.kakao.linknamu._core.util.AuditingEntity;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name="bookmark_tab_tb")
public class BookmarkTag extends AuditingEntity {

    @EmbeddedId
    private BookmarkTagId bookmarkTagId;

    @MapsId("bookmarkId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;


    @Builder
    public BookmarkTag(Bookmark bookmark, Tag tag) {
        this.bookmark = bookmark;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkTag that = (BookmarkTag) o;
        return Objects.equals(bookmarkTagId, that.bookmarkTagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookmarkTagId);
    }
}
