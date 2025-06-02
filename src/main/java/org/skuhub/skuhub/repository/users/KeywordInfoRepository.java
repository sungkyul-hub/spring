package org.skuhub.skuhub.repository.users;

import org.skuhub.skuhub.model.user.KeywordInfoJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordInfoRepository extends JpaRepository<KeywordInfoJpaEntity, Long> {
    @Query("SELECT k FROM KeywordInfoJpaEntity k WHERE k.keyword LIKE %:keyword%")
    List<KeywordInfoJpaEntity> findByKeyword(String keyword);

    @Query("SELECT k FROM KeywordInfoJpaEntity k WHERE :title LIKE CONCAT('%', k.keyword, '%')")
    List<KeywordInfoJpaEntity> findKeywordsWithinTitle(@Param("title") String title);

    @Query("SELECT k FROM KeywordInfoJpaEntity k WHERE k.userKey = :userKeyId AND k.keyword = :keyword")
    Optional<KeywordInfoJpaEntity> findByUserKeyAndKeyword(UserInfoJpaEntity userKeyId, String keyword);

    @Query("SELECT k FROM KeywordInfoJpaEntity k WHERE k.keyword = :keyword")
    Optional<KeywordInfoJpaEntity> findByUserKeyAndKeyword1(Long keyword);



    List<KeywordInfoJpaEntity> findByUserKey(UserInfoJpaEntity userEntity);
}
