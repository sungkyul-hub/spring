package org.skuhub.skuhub.api.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatedUserInfoResponse {
    private String userId;
    private String email;
    private String department;
    private String name;
}
