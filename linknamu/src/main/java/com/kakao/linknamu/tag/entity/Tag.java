package com.kakao.linknamu.tag.entity;

import com.kakao.linknamu.core.util.AuditingEntity;
import com.kakao.linknamu.user.entity.User;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(
	name = "tag_tb",
	uniqueConstraints = {
		@UniqueConstraint
			(
				name = "user_id_tag_name unique_constraint",
				columnNames = {"user_id", "tag_name"}
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
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@Column(length = 50, nullable = false, name = "tag_name")
	private String tagName;

	@Builder
	public Tag(Long tagId, User user, String tagName) {
		this.tagId = tagId;
		this.user = user;
		this.tagName = tagName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Tag tag = (Tag)obj;
		return Objects.equals(tagId, tag.tagId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tagId);
	}
}
