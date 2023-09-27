package com.kakao.linknamu.category;

import com.kakao.linknamu.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name="category_tb",
        uniqueConstraints={
                @UniqueConstraint(
                        name = "parentCategory_categoryName unique constraint",
                        columnNames = {
                                "parent_category_id",
                                "category_name"
                        }
                )
        }
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100, nullable = false, name = "category_name")
    private String categoryName;


    @Builder
    public Category(Long categoryId, Category category, User user, String categoryName) {
        this.categoryId = categoryId;
        this.parentCategory = category;
        this.user = user;
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category1 = (Category) o;
        return Objects.equals(categoryId, category1.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }
}
