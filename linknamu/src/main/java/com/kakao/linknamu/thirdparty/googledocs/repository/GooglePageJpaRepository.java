package com.kakao.linknamu.thirdparty.googledocs.repository;

import com.kakao.linknamu.thirdparty.googledocs.entity.GooglePage;
import com.kakao.linknamu.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GooglePageJpaRepository extends JpaRepository<GooglePage, Long> {
	boolean existsByDocumentIdAndUser(String documentId, User user);

	@Query(value = "select gp from GooglePage gp where gp.isActive=true")
	List<GooglePage> findByActivePage();
}
