package org.skuhub.skuhub.api.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private String password;
    private int year;
    private String department;
    private String name;
}
