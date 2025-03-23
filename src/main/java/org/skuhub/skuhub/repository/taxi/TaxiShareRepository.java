package org.skuhub.skuhub.repository.taxi;

import org.skuhub.skuhub.api.taxi.dto.response.TaxiPostResponse;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxiShareRepository extends JpaRepository<TaxiShareJpaEntity, Long> {
    @Query("SELECT ts FROM TaxiShareJpaEntity ts WHERE ts.createdAt >= :startOfDay AND ts.createdAt < :endOfDay AND ts.postId < :cursor ORDER BY ts.postId DESC")
    List<TaxiShareJpaEntity> findAllByCreatedAtToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay,
                                                     @Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT p FROM TaxiShareJpaEntity p LEFT JOIN FETCH p.commentTbs WHERE p.postId = :postId")
    Optional<TaxiShareJpaEntity> findPostWithComments(@Param("postId") Long postId);

//    @Query("SELECT p FROM TaxiShareJpaEntity p WHERE p.title LIKE CONCAT('%', :keyword, '%')")
//    List<TaxiShareJpaEntity> findByTitle(@Param("keyword") String keyword);

    @Query("SELECT t FROM TaxiShareJpaEntity t WHERE t.title LIKE %:keyword% AND t.postId < :cursor ORDER BY t.postId DESC")
    List<TaxiShareJpaEntity> findByTitle(@Param("keyword") String keyword, @Param("cursor") Long cursor, Pageable pageable);

    @Query("SELECT t FROM TaxiShareJpaEntity t ORDER BY t.postId DESC")
    List<TaxiShareJpaEntity> findLastPostId();

}
