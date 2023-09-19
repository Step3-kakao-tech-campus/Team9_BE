package com.kakao.linknamu.Category;

import com.kakao.linknamu.User.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="category_tb")
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(length = 100, nullable = false, name = "category_name")
    private String categoryName;


    @Builder
    public Category(Long categoryId, Category category, User user, String categoryName) {
        this.categoryId = categoryId;
        this.parentId = category;
        this.userId = user;
        this.categoryName = categoryName;
    }
}
