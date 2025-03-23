package org.skuhub.skuhub.common.utills.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtDto {
    @Schema(description = "유저 ID", example = "USER-XXXXX...")
    private String userId;
    @Schema(description = "엑세스 토큰", example = "accessToken(20min)")
    private String accessToken;
    @Schema(description = "리프레쉬 토큰", example = "refreshToken(4weeks)")
    private String refreshToken;
    @Schema(description = "토큰 종류", example = "Bearer")
    private String grantType;
    @Schema(description = "만료 시간", example = "20min")
    private Long expiresIn;
}