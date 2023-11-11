package com.kakao.linknamu.workspace.repository;

import com.kakao.linknamu.workspace.entity.Workspace;
import com.kakao.linknamu.workspace.entity.constant.LinkProvider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {

	@Query(value = "select w from Workspace w where w.user.userId=:userId and w.workspaceName =:workspaceName")
	Optional<Workspace> findByUserIdAndWorkspaceName(
		@Param("userId") Long userId,
		@Param("workspaceName") String workspaceName);

	@Query(value = "select w from Workspace w "
		+ "where w.user.userId =:userId")
	List<Workspace> findAllByUserId(@Param("userId") Long userId);

	@Query(value = "select w from Workspace w "
		+ "where w.user.userId =:userId and w.linkProvider = :provider")
	Optional<Workspace> findByUserIdAndProvider(@Param("userId") Long userId,
		@Param("provider") LinkProvider provider);
}
