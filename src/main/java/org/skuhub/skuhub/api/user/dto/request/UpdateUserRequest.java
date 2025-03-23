package org.skuhub.skuhub.api.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class UpdateUserRequest { // 새 비밀번호
    private String department;       // 새 학과 이름
    private String name;
}
