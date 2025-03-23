package org.skuhub.skuhub.api.timetable.service;

import java.util.List;
import org.skuhub.skuhub.model.timetable.TimetableScheduleEntity;
import org.skuhub.skuhub.api.timetable.dto.request.UserTimetableRequest;
import org.skuhub.skuhub.api.timetable.dto.response.UserTimetableResponse;
import org.skuhub.skuhub.api.timetable.dto.request.UpdateUserTimetableRequest;
import org.skuhub.skuhub.api.timetable.dto.request.UpdateScoreRequest;
import org.skuhub.skuhub.api.timetable.dto.request.CompletionRequest;
import org.skuhub.skuhub.model.timetable.CompletionEntity;
import org.skuhub.skuhub.model.timetable.PersonalTimetableEntity;

public interface TimetableService {

    List<TimetableScheduleEntity> getAllSchedules();

    void createUserTimetable(UserTimetableRequest request);

    List<UserTimetableResponse> getUserTimetable(Integer userKey);

    boolean updateUserTimetable(UpdateUserTimetableRequest request);

    boolean deleteUserTimetable(Integer personalKey);

    boolean addScoreToUserTimetable(Integer personalKey, String score, Integer majorStatus);

    boolean updateUserScore(UpdateScoreRequest request);

    CompletionEntity saveCompletionData(CompletionRequest request);

    CompletionEntity updateCompletionData(CompletionRequest request);


    List<PersonalTimetableEntity> getUserPersonalTimetable(Integer userKey);


    List<TimetableScheduleEntity> findSchedulesByConditions(
            Integer year,
            Integer semester,
            String department,
            Integer grade,
            Integer subjectCode,
            String subjectName,
            String professorName,
            String classTime,
            String classroom);
}
