package org.skuhub.skuhub.api.timetable.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CompletionRequest {
    @JsonProperty("user_key")
    private Long userKey;

    @JsonProperty("major_required")
    private Integer majorRequired;

    @JsonProperty("major_elective")
    private Integer majorElective;

    @JsonProperty("general_required")
    private Integer generalRequired;

    @JsonProperty("general_elective")
    private Integer generalElective;

    @JsonProperty("major_required_req")
    private Integer majorRequiredReq;

    @JsonProperty("major_elective_req")
    private Integer majorElectiveReq;

    @JsonProperty("general_required_req")
    private Integer generalRequiredReq;

    @JsonProperty("general_elective_req")
    private Integer generalElectiveReq;

    @JsonProperty("other")
    private Integer other;

    @JsonProperty("earned_credits")
    private Integer earnedCredits;

    @JsonProperty("graduation_credits")
    private Integer graduationCredits;

    @JsonProperty("avg_score")
    private BigDecimal avgScore;
}
