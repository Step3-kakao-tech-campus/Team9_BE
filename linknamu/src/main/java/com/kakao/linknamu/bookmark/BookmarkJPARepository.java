package com.kakao.linknamu.bookmark;

import com.kakao.linknamu.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkJPARepository extends JpaRepository<User, Integer> {

}
