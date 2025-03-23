package org.skuhub.skuhub.api.schedule.service;

import org.skuhub.skuhub.model.schedule.ScheduleEntity;
import java.time.Instant;
import java.util.List;

public interface ScheduleService {
    List<ScheduleEntity> getSchedulesByTargetDate(Instant target);
}
