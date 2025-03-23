package org.skuhub.skuhub.api.schedule.service;

import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.model.schedule.ScheduleEntity;
import org.skuhub.skuhub.repository.schedule.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public List<ScheduleEntity> getSchedulesByTargetDate(Instant target) {
        return scheduleRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(target, target);
    }
}
