package org.skuhub.skuhub.repository.users;

import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoJpaEntity, Long> {
    boolean existsByEmail(String email);
    
    Optional<UserInfoJpaEntity> findByEmail(String email);

    boolean existsByUserId(String userId);
    Optional<UserInfoJpaEntity> findByUserId(String userId); // userId로 찾는 메소드

    Optional<UserInfoJpaEntity> findByUserKey(Long userKey);
}

