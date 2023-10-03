package com.kakao.linknamu.workspace.entity;

import com.kakao.linknamu.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(exclude = {"user"})
@Table(
        name = "workspace_tb",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="user_workspaceName unique constraint",
                        columnNames = {
                                "user_id",
                                "workspace_name"
                        }
                )
        }
)
public class Workspace {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="workspace_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50, name = "workspace_name")
    private String workspaceName;

    @Builder
    public Workspace(Long id, User user, String workspaceName) {
        this.id = id;
        this.user = user;
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
