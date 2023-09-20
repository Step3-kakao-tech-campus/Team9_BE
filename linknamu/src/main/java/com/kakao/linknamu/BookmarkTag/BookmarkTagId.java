package com.kakao.linknamu.BookmarkTag;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class BookmarkTagId implements Serializable {
    private Long bookmarkId;
    private Long tagId;

    public BookmarkTagId(Long bookmarkId, Long tagId) {
        this.bookmarkId = bookmarkId;
        this.tagId = tagId;
    }
}
