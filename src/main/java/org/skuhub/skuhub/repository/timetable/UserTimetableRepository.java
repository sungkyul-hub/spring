package org.skuhub.skuhub.repository.timetable;

import org.skuhub.skuhub.model.timetable.UserTimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTimetableRepository extends JpaRepository<UserTimetableEntity, Integer> {
    List<UserTimetableEntity> findByUserKey(Integer userKey);
}
