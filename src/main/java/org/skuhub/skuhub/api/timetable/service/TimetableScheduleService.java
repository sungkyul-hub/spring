package org.skuhub.skuhub.api.timetable.service;

import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.api.timetable.dto.response.TimetableScheduleResponse;
import org.skuhub.skuhub.model.timetable.TimetableScheduleEntity;
import org.skuhub.skuhub.repository.timetable.TimetableScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableScheduleService {

    private final TimetableScheduleRepository timetableScheduleRepository;

    /**
     * 전체 시간표 조회 (모든 강의 관련 데이터 반환)
     */
    public List<TimetableScheduleResponse> getAllSchedules() {
        List<TimetableScheduleEntity> schedules = timetableScheduleRepository.findAll();
        return schedules.stream()
                .map(TimetableScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 조건 검색 기능 추가
     */
    public List<TimetableScheduleResponse> findSchedulesByConditions(
            Integer year, Integer semester, String department, Integer grade, Integer subjectCode,
            String subjectName, String professorName, String classTime, String classroom
    ) {
        List<TimetableScheduleEntity> schedules = timetableScheduleRepository.findSchedulesByConditions(
                year, semester, department, grade, subjectCode, subjectName, professorName, classTime, classroom
        );

        return schedules.stream()
                .map(TimetableScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
