package com.kakao.linknamu.category.entity;

import com.kakao.linknamu._core.util.AuditingEntity;
import com.kakao.linknamu.bookmark.entity.Bookmark;
import com.kakao.linknamu.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
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
public class Category extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Category parentCategory;

    // 카테고리 삭제 시 자녀 카테고리 삭제
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.REMOVE)
    private List<Category> childCategories;

    // 카테고리 삭제 시 카테고리에 속한 북마크 삭제
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(length = 100, nullable = false, name = "category_name")
    private String categoryName;

    // 루트 카테고리 지정
    public void setRootCategory() {
        this.parentCategory = this;
    }

    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Builder
    public Category(Long categoryId, Category parentCategory, User user, String categoryName) {
        this.categoryId = categoryId;
        this.parentCategory = parentCategory;
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
