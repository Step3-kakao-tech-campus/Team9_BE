package com.kakao.linknamu.bookmark.repository;

import com.kakao.linknamu.bookmark.entity.Bookmark;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookmarkCustomRepositoryImpl implements BookmarkCustomRepository{
    private static final String TABLE = "bookmark_tb";

    private final EntityManager em;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void bookmarkBatchInsert(List<Bookmark> bookmarkList) {
        // 중복되는 값이 있다면 무시한다.
        String insertQuery = String.format("INSERT IGNORE INTO %s ( category_id, bookmark_name, bookmark_link, " +
                "bookmark_description, bookmark_thumbnail, created_at, last_modified_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", TABLE);

        BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Bookmark bookmark = bookmarkList.get(i);
                ps.setLong(1, bookmark.getCategory().getCategoryId());
                ps.setString(2, bookmark.getBookmarkName());
                ps.setString(3, bookmark.getBookmarkLink());
                ps.setString(4, bookmark.getBookmarkDescription());
                ps.setString(5, bookmark.getBookmarkThumbnail());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return bookmarkList.size();
            }
        };

        jdbcTemplate.getJdbcTemplate().batchUpdate(insertQuery, batchSetter);
        em.flush();
        em.clear();
    }
}
