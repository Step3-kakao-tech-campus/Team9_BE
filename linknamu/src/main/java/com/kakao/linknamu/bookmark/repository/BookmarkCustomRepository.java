package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;

import java.util.List;

public interface BookmarkCustomRepository {
    void bookmarkBatchInsert(List<Bookmark> bookmarkList);
}
