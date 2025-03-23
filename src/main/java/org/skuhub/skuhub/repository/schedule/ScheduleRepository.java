package org.skuhub.skuhub.repository.schedule;

import org.skuhub.skuhub.model.schedule.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {

    List<ScheduleEntity> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(Instant target, Instant target2);
}
