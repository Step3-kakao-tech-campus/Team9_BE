package com.kakao.linknamu.Bookmark;

import com.kakao.linknamu.Category.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="bookmark_tb")
public class Bookmark {

    @Id
    @GeneratedValue
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 100, nullable = false, name = "bookmark_name")
    private String bookmarkName;

    @Column(length = 1024, nullable = false, name = "bookmark_link")
    private String bookmarkLink;

    @Column(length = 200, name = "bookmark_description")
    private String bookmarkDescription;

    @Column(length = 512, name = "bookmark_thumbnail")
    private String bookmarkThumbnail;


    @Builder
    public Bookmark(Long bookmarkId, Category category, String bookmarkName, String bookmarkLink, String bookmarkDescription, String bookmarkThumbnail) {
        this.bookmarkId = bookmarkId;
        this.category = category;
        this.bookmarkName = bookmarkName;
        this.bookmarkLink = bookmarkLink;
        this.bookmarkDescription = bookmarkDescription;
        this.bookmarkThumbnail = bookmarkThumbnail;
    }
}
