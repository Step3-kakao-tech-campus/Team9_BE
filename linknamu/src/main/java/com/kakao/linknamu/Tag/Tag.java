package com.kakao.linknamu.Tag;

import com.kakao.linknamu.User.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="tag_tb")
public class Tag {

    @Id
    @GeneratedValue
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
}
