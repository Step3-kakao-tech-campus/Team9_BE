package com.kakao.linknamu.bookmark.repository;

import java.util.List;

import com.kakao.linknamu.bookmark.entity.Bookmark;

public interface BookmarkCustomRepository {
	void batchInsertBookmark(List<Bookmark> bookmarkList);
}
