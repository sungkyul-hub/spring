package org.skuhub.skuhub.api.user.service;

import org.skuhub.skuhub.api.user.dto.request.UpdateUserRequest;
import org.skuhub.skuhub.api.user.dto.response.UpdatedUserInfoResponse;
import org.skuhub.skuhub.api.user.dto.response.UserInfoResponse;

public interface UserService {
    String findUserIdByEmail(String email);

    boolean isUserValid(String userId);

    UpdatedUserInfoResponse updateUserInfo(String userId, UpdateUserRequest request);

    UserInfoResponse getUserInfo(String userId);

    boolean withdrawUser(String userId);


}
