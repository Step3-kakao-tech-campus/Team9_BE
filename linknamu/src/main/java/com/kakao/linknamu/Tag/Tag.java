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
    private Long tag_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(length = 50, nullable = false, unique = true, name = "tag_name")
    private String tag_name;


    @Builder
    public Tag(Long tag_id, User user, String tag_name) {
        this.tag_id = tag_id;
        this.user_id = user;
        this.tag_name = tag_name;
    }
}
