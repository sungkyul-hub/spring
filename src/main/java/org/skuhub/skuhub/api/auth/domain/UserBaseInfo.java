package org.skuhub.skuhub.api.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBaseInfo {
    private String userId;
    private String email;
    private String password;
}
