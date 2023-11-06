package com.kakao.linknamu.thirdparty.notion.repository;

import com.kakao.linknamu.thirdparty.notion.entity.NotionAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotionAccountJpaRepository extends JpaRepository<NotionAccount, Long> {

	@Query(value = "select n from NotionAccount n "
		+ "where n.user.userId = :userId and n.token = :accessToken")
	Optional<NotionAccount> findByUserIdAndAccessToken(@Param("userId") Long userId,
													   @Param("accessToken") String accessToken);
}
