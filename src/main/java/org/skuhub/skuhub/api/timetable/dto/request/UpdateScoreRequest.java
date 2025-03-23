package org.skuhub.skuhub.api.timetable.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateScoreRequest {
    @JsonProperty("personal_key")
    private Integer personalKey;

    @JsonProperty("score")
    private String score;

    @JsonProperty("major_status")
    private Integer majorStatus;
}
