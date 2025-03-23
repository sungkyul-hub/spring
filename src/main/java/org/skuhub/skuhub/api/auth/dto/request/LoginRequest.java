package org.skuhub.skuhub.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(description = "이메일", example = "abcde@gmail.com")
    private String email;

    @Schema(description = "비밀번호", example = "password")
    private String password;
}