package org.skuhub.skuhub.repository.timetable;

import org.skuhub.skuhub.model.timetable.CompletionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompletionRepository extends JpaRepository<CompletionEntity, Integer> {
    Optional<CompletionEntity> findByUserInfoTb_UserKey(Long userKey);
}