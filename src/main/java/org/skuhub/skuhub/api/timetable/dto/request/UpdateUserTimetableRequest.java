package org.skuhub.skuhub.api.timetable.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserTimetableRequest {
    @JsonProperty("personal_key")
    private Integer personalKey;

    @JsonProperty("user_key")
    private Integer userKey;

    @JsonProperty("timetable_id")
    private Integer timetableId;

    @JsonProperty("personal_grade")
    private String personalGrade;

    @JsonProperty("personal_semester")
    private String personalSemester;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("score")
    private String score;

    @JsonProperty("major_status")
    private Integer majorStatus;
}

