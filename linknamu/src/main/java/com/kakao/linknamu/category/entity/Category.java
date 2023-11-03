package com.kakao.linknamu.category.entity;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.kakao.linknamu.core.util.AuditingEntity;
import com.kakao.linknamu.workspace.entity.Workspace;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
	name = "category_tb",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "workspace_categoryName unique constraint",
			columnNames = {
				"workspace_id",
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
	@JoinColumn(name = "workspace_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Workspace workspace;

	@Column(length = 100, nullable = false, name = "category_name")
	private String categoryName;

	public void updateCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Builder
	public Category(Long categoryId, Workspace workspace, String categoryName) {
		this.categoryId = categoryId;
		this.workspace = workspace;
		this.categoryName = categoryName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Category category1 = (Category)o;
		return Objects.equals(categoryId, category1.categoryId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryId);
	}
}
