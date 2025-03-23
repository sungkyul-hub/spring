package org.skuhub.skuhub.api.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class UserInfoResponse {

    private String department;

    private int year;

    private String name;

}
