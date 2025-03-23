package org.skuhub.skuhub.api.timetable.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserTimetableRequest {

    @JsonProperty("personal_key")  // JSON 요청에서 올바르게 매핑되도록 설정
    private Integer personalKey;
}
