package org.skuhub.skuhub.api.push.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class TokenSaveRequest {
    @NotBlank(message="토큰을 입력해야 합니다.")
    @Schema(description = "FCM Token", example = "")
    String token;
}
