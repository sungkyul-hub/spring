package org.skuhub.skuhub.api.timetable.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.skuhub.skuhub.model.timetable.PersonalTimetableEntity;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class PersonalTimetableResponse {
    private Integer personalKey;
    private Integer userKey;
    private Integer timetableId;
    private String personalGrade;
    private String personalSemester;
    private Instant createdAt;
    private String score;
    private Integer majorStatus;

    /**
     * PersonalTimetableEntity → PersonalTimetableResponse 변환 메서드
     */
    public static PersonalTimetableResponse fromEntity(PersonalTimetableEntity entity) {
        return PersonalTimetableResponse.builder()
                .personalKey(entity.getPersonalKey())
                .userKey(entity.getUserKey())
                .timetableId(entity.getTimetableId())
                .personalGrade(entity.getPersonalGrade())
                .personalSemester(entity.getPersonalSemester())
                .createdAt(entity.getCreatedAt().toInstant())
                .score(entity.getScore())
                .majorStatus(entity.getMajorStatus())
                .build();
    }
}
