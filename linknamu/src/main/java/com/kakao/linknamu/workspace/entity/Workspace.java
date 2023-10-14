package com.kakao.linknamu.workspace.entity;

import com.kakao.linknamu._core.util.AuditingEntity;
import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(callSuper = true, exclude = {"user", "categorySet"})
@DynamicUpdate
@Table(
        name = "workspace_tb",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_workspaceName unique constraint",
                        columnNames = {
                                "user_id",
                                "workspace_name"
                        }
                )
        }
)
public class Workspace extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(length = 50, name = "workspace_name", nullable = false)
    private String workspaceName;

    @OneToMany(mappedBy = "workspace")
    private Set<Category> categorySet = new HashSet<>();

    @Builder
    public Workspace(Long id, User user, String workspaceName) {
        this.id = id;
        this.user = user;
        this.workspaceName = workspaceName;
    }

    public void renameWorkspace(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workspace workspace = (Workspace) o;
        return Objects.equals(getId(), workspace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}






