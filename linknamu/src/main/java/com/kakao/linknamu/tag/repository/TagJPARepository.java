package com.kakao.linknamu.tag.repository;

import com.kakao.linknamu.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TagJPARepository extends JpaRepository<Tag, Long> {
    @Query("select t from Tag t where t.tagName = :tagName")
    Optional<Tag> findByName(@Param("tagName") String tagName);
}
