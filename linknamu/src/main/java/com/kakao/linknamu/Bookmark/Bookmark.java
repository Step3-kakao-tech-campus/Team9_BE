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
    private Long bookmark_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 100, nullable = false, name = "bookmark_name")
    private String bookmark_name;

    @Column(length = 1024, nullable = false, name = "bookmark_link")
    private String bookmark_link;

    @Column(length = 200, name = "bookmark_description")
    private String bookmark_description;

    @Column(length = 512, name = "bookmark_thumbnail")
    private String bookmark_thumbnail;


    @Builder
    public Bookmark(Long bookmark_id, Category category, String bookmark_name, String bookmark_link, String bookmark_description, String bookmark_thumbnail) {
        this.bookmark_id = bookmark_id;
        this.category = category;
        this.bookmark_name = bookmark_name;
        this.bookmark_link = bookmark_link;
        this.bookmark_description = bookmark_description;
        this.bookmark_thumbnail = bookmark_thumbnail;
    }
}
