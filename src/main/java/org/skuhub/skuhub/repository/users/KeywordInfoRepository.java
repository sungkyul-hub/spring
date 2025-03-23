package org.skuhub.skuhub.repository.users;

import org.skuhub.skuhub.model.user.KeywordInfoJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordInfoRepository extends JpaRepository<KeywordInfoJpaEntity, Long> {
    @Query("SELECT k FROM KeywordInfoJpaEntity k WHERE k.keyword LIKE %:keyword%")
    List<KeywordInfoJpaEntity> findByKeyword(String notice);

    @Query("SELECT k FROM KeywordInfoJpaEntity k WHERE k.userKey = :userKey AND k.keyword = :keyword")
    Optional<KeywordInfoJpaEntity> findByUserKeyAndKeyword(Long userKey, String keyword);
}
