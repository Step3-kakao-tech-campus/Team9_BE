package com.kakao.linknamu.googleDocs.repository;

import com.kakao.linknamu.googleDocs.entity.GooglePage;
import com.kakao.linknamu.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GooglePageJPARepository extends JpaRepository<GooglePage, Long> {
    boolean existsByDocumentIdAndUser(String documentId, User user);
}
