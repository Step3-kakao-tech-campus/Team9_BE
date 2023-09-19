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
@Table(name="bookmark_tab_tb")
public class Bookmark_Tag {

    @Id
    @ManyToOne
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;


    @Builder
    public Bookmark_Tag(Bookmark bookmark, Tag tag) {
        this.bookmark = bookmark;
        this.tag = tag;
    }
}
