package org.skuhub.skuhub.api.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.api.user.dto.request.UpdateUserRequest;
import org.skuhub.skuhub.api.user.dto.response.UpdatedUserInfoResponse;
import org.skuhub.skuhub.api.user.dto.response.UserInfoResponse;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;
import org.skuhub.skuhub.exceptions.CustomException;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.skuhub.skuhub.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public String findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserInfoJpaEntity::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public boolean isUserValid(String userId) {
        return userRepository.existsByUserId(userId); // userId가 존재하는지 확인
    }


    @Transactional
    @Override
    public UpdatedUserInfoResponse updateUserInfo(String userId, UpdateUserRequest request) {
        UserInfoJpaEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (request.getDepartment() != null && !request.getDepartment().isEmpty()) {
            user.setDepartment(request.getDepartment());
        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }

        userRepository.save(user);

        return new UpdatedUserInfoResponse(user.getUserId(), user.getEmail(), user.getDepartment(), user.getName());
    }


    @Transactional
    @Override
    public UserInfoResponse getUserInfo(String userId) {
        UserInfoJpaEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return new UserInfoResponse(user.getDepartment(), user.getYear(), user.getName());
    }

    @Transactional
    @Override
    public boolean withdrawUser(String userId) {
        UserInfoJpaEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        userRepository.delete(user);

        return true;
    }

}