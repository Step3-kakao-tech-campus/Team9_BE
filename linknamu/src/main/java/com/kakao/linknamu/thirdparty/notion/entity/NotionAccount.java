package com.kakao.linknamu.thirdparty.notion.entity;

import com.kakao.linknamu.core.util.AuditingEntity;
import com.kakao.linknamu.user.entity.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "notion_account_tb",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_user_token", columnNames = {"user_id", "token"})
	}
)

public class NotionAccount extends AuditingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notion_account_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "token", nullable = false)
	private String token;

	@Builder
	public NotionAccount(Long id, User user, String token) {
		this.id = id;
		this.user = user;
		this.token = token;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		NotionAccount notion = (NotionAccount)obj;
		return Objects.equals(getId(), notion.getId()) && Objects.equals(getToken(), notion.getToken());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getToken());
	}
}
