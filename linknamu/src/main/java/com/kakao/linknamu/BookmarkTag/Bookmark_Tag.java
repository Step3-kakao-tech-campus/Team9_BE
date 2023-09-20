package com.kakao.linknamu.BookmarkTag;

import com.kakao.linknamu.Bookmark.Bookmark;
import com.kakao.linknamu.Tag.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name="bookmark_tab_tb",
        uniqueConstraints={
                @UniqueConstraint(
                        name = "categoryId_bookmarkLink unique constraint",
                        columnNames = {
                                "category_id",
                                "bookmark_link"
                        }
                )
        }
)
public class Bookmark_Tag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;


    @Builder
    public Bookmark_Tag(Bookmark bookmark, Tag tag) {
        this.bookmark = bookmark;
        this.tag = tag;
    }
}
