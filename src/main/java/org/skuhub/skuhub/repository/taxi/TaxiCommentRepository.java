package org.skuhub.skuhub.repository.taxi;

import org.skuhub.skuhub.model.taxi.TaxiCommentJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxiCommentRepository extends JpaRepository<TaxiCommentJpaEntity, Long> {
    List<TaxiCommentJpaEntity> findByPostId(TaxiShareJpaEntity postId);
}
