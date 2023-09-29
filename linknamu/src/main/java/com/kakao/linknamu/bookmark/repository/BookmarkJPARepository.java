package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkJPARepository extends JpaRepository<Bookmark, Long> {
    @Query("select b from Bookmark b join b.bookmarkName t where t.tagName in :tags")
    List<Bookmark> findBookmarksByTags(@Param("tags") List<String> tags);

    Optional<List<Bookmark>> findByBookmarkName(String bookmarkName);
}
