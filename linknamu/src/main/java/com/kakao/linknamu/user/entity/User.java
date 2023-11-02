package com.kakao.linknamu.user.entity;

import java.util.Objects;

import com.kakao.linknamu.core.util.AuditingEntity;
import com.kakao.linknamu.user.entity.constant.Provider;
import com.kakao.linknamu.user.entity.constant.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_tb")
public class User extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(length = 100, nullable = false, unique = true, name = "email")
	private String email;

	@Column(nullable = false, name = "password")
	private String password;

	@Column(length = 30, nullable = false, name = "provider")
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Column(length = 30, nullable = false, name = "role")
	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder
	public User(Long userId, String email, String password, Provider provider, Role role) {
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.provider = provider;
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return Objects.equals(getUserId(), user.getUserId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId());
	}
}
