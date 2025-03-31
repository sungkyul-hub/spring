package org.skuhub.skuhub.repository.notice;
import org.skuhub.skuhub.api.notice.dto.response.NoticeResponse;
import org.skuhub.skuhub.model.notice.NoticeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeJpaEntity, Long> {
    @Query("SELECT p FROM NoticeJpaEntity p WHERE p.title LIKE %:keyword% AND (:cursor IS NULL OR p.id < :cursor)")
    List<NoticeJpaEntity> findByTitle(@Param("keyword") String keyword,
                                      @Param("cursor") Long cursor,
                                      Pageable pageable);

    @Query("SELECT p FROM NoticeJpaEntity p WHERE p.noticeCategory LIKE :keyword AND  p.id < :cursor ORDER BY p.noticeModifyDate DESC ")
    List<NoticeJpaEntity> findByCategory(@Param("keyword") String category,
                                         @Param("cursor") Long cursor,
                                         Pageable pageable);

    @Query("SELECT n FROM NoticeJpaEntity n WHERE n.title LIKE %:keyword% AND  n.id < :cursor ORDER BY n.noticeModifyDate DESC ")
    List<NoticeJpaEntity> findByTitleWithCursor(@Param("keyword") String keyword,
                                                @Param("cursor") Long cursor,
                                                Pageable pageable);

    @Query("SELECT n FROM NoticeJpaEntity n  ORDER BY n.noticeModifyDate DESC ")
    List<NoticeJpaEntity> findLastNoticeId();

    boolean existsByTitle(String title);
}
