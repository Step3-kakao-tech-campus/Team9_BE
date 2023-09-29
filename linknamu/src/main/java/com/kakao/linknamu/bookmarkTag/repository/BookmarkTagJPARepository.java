package com.kakao.linknamu.bookmarkTag.repository;

import com.kakao.linknamu.bookmarkTag.entity.BookmarkTag;
import com.kakao.linknamu.bookmarkTag.entity.BookmarkTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkTagJPARepository extends JpaRepository<BookmarkTag, BookmarkTagId> {

}
