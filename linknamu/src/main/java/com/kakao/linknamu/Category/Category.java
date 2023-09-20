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
    private Long category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category parentCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100, nullable = false, unique = true, name = "category_name")
    private String categoryName;


    @Builder
    public Category(Long categoryId, Category category, User user, String categoryName) {
        this.category = categoryId;
        this.parentCategory = category;
        this.user = user;
        this.categoryName = categoryName;
    }
}
