package com.kakao.linknamu.tag.repository;

import com.kakao.linknamu.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagJPARepository extends JpaRepository<Tag, Long> {
    @Query("select t from Tag t where t.tagName = :tagName")
    Optional<Tag> findByName(@Param("tagName") String tagName);

    @Query("select t.tagName from Tag t where t.tagId = :tagId")
    String findNameById(@Param("tagId") Long tagId);

    @Query("select t.tagId from Tag t where t.tagName = :tagName")
    List<Long> findIdsByName(@Param("tagName") String name);

    @Query("select t from Tag t where t.user.userId = :userId and t.tagName = :name")
    Optional<Tag> findByUserIdAndName(@Param("userId") Long userId, @Param("name") String name);
}
