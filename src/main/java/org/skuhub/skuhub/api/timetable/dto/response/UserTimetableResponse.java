package org.skuhub.skuhub.api.timetable.dto.response;

import lombok.Builder;
import lombok.Data;
import org.skuhub.skuhub.model.timetable.UserTimetableEntity;

import java.time.Instant;

@Data
@Builder
public class UserTimetableResponse {
    private Integer personalKey;
    private Integer userKey;
    private Integer timetableId;
    private String personalGrade;
    private String personalSemester;
    private Instant createdAt;
    private String score;
    private Integer majorStatus;

    public static UserTimetableResponse fromEntity(UserTimetableEntity entity) {
        return UserTimetableResponse.builder()
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
