package org.skuhub.skuhub.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IdentityRequest {
    @Schema(description = "입학년도", example = "2023")
    private int year;

    @Schema(description = "학과", example = "컴퓨터 공학")
    private String department;
}
