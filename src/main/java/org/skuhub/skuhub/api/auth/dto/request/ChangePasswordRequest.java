package org.skuhub.skuhub.api.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ChangePasswordRequest {
    private String email;           // 이메일
    private String newPassword;      // 새로운 비밀번호
}
