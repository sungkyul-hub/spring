package org.skuhub.skuhub.api.auth.service;

import org.skuhub.skuhub.api.auth.dto.request.ChangePasswordRequest;
import org.skuhub.skuhub.api.auth.dto.request.SignupRequest;
import org.skuhub.skuhub.common.utills.jwt.dto.JwtDto;
import org.skuhub.skuhub.api.auth.dto.request.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    JwtDto signup(SignupRequest request);

    JwtDto login(LoginRequest request);

    JwtDto reissue(String refreshToken);

    boolean getEmailValidity(String email);

    void changePassword(ChangePasswordRequest request);

    boolean isUserIdAvailable(String userId);

    boolean isPasswordValid(String userId, String password);
}
