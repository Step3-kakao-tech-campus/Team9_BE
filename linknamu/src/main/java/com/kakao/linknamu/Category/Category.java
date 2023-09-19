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
    private Long category_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category parent_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(length = 100, nullable = false, name = "category_name")
    private String category_name;


    @Builder
    public Category(Long category_id, Category category, User user, String category_name) {
        this.category_id = category_id;
        this.parent_id = category;
        this.user_id = user;
        this.category_name = category_name;
    }
}
