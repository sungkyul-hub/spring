package org.skuhub.skuhub.repository.taxi;

import org.skuhub.skuhub.model.taxi.TaxiJoinJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxiJoinRepository extends JpaRepository<TaxiJoinJpaEntity, Long> {
    Optional<TaxiJoinJpaEntity> findByPostIdAndUserKey(TaxiShareJpaEntity joinTaxi, UserInfoJpaEntity userEntity);

    Optional<TaxiJoinJpaEntity> findByUserKey(UserInfoJpaEntity userEntity);

    List<TaxiJoinJpaEntity> findByPostId(TaxiShareJpaEntity shareEntity);
}
