package com.kakao.linknamu.googleDocs.entity;

import com.kakao.linknamu._core.util.AuditingEntity;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class GooglePage extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "document_id", nullable = false)
    private String documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "page_name", nullable = false)
    private String pageName;

    @Builder
    public GooglePage(Long id, User user, String documentId, Category category, String pageName, Boolean isActive) {
        this.id = id;
        this.user = user;
        this.documentId = documentId;
        this.category = category;
        this.pageName = pageName;
        this.isActive = isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GooglePage that = (GooglePage) o;
        return Objects.equals(id, that.id) && Objects.equals(pageName, that.getPageName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPageName());
    }
}
