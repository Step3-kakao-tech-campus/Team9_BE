package com.kakao.linknamu.tag.entity;

import com.kakao.linknamu._core.util.AuditingEntity;
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
        name="tag_tb",
        uniqueConstraints={
                @UniqueConstraint(
                        name = "userId_tagName unique constraint",
                        columnNames = {
                                "user_id",
                                "tag_name"
                        }
                )
        }
)
public class Tag extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50, nullable = false, unique = true, name = "tag_name")
    private String tagName;


    @Builder
    public Tag(Long tagId, User user, String tagName) {
        this.tagId = tagId;
        this.user = user;
        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagId, tag.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId);
    }
}
