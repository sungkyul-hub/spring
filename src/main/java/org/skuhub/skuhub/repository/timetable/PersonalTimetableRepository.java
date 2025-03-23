package org.skuhub.skuhub.repository.timetable;

import org.skuhub.skuhub.model.timetable.PersonalTimetableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalTimetableRepository extends JpaRepository<PersonalTimetableEntity, Integer> {
    List<PersonalTimetableEntity> findByUserKey(Integer userKey);
}
