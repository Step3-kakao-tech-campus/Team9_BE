package com.kakao.linknamu.Bookmark;

import com.kakao.linknamu.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJPARepository extends JpaRepository<User, Integer> {

}
