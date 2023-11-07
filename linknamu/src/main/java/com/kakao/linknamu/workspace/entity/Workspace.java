package com.kakao.linknamu.workspace.entity;

import com.kakao.linknamu.category.entity.Category;
import com.kakao.linknamu.core.util.AuditingEntity;
import com.kakao.linknamu.user.entity.User;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;
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
		@UniqueConstraint
			(
				name = "user_workspaceName unique_constraint",
				columnNames = {"user_id", "workspace_name"}
			),
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

	@Enumerated(EnumType.STRING)
	@Column(length = 20, name = "link_provider", nullable = false)
	private LinkProvider linkProvider;

	@Builder
	public Workspace(Long id, User user, String workspaceName, LinkProvider linkProvider) {
		this.id = id;
		this.user = user;
		this.workspaceName = workspaceName;
		this.linkProvider = linkProvider;
	}

	public void renameWorkspace(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	public void setLinkProvider(LinkProvider linkProvider) {
		this.linkProvider = linkProvider;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Workspace workspace = (Workspace) obj;
		return Objects.equals(getId(), workspace.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}






