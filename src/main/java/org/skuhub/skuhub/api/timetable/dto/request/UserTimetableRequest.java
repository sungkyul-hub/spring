package org.skuhub.skuhub.api.timetable.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserTimetableRequest {

    @JsonProperty("personal_key")
    private Integer personalKey;

    @JsonProperty("user_key")
    private Integer userKey;

    @JsonProperty("timetable_id")
    private Integer timetableId;

    @NotNull
    @JsonProperty("personal_grade")
    private String personalGrade;

    @NotNull
    @JsonProperty("personal_semester")
    private String personalSemester;

    @NotNull
    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("score")
    private String score;

    @JsonProperty("major_status")
    private Integer majorStatus;
}
