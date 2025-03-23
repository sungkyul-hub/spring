package org.skuhub.skuhub.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.api.auth.dto.request.ChangePasswordRequest;
import org.skuhub.skuhub.api.auth.dto.request.SignupRequest;
import org.skuhub.skuhub.api.auth.dto.request.LoginRequest;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.skuhub.skuhub.common.utills.jwt.dto.JwtDto;
import org.skuhub.skuhub.exceptions.CustomException;

import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.skuhub.skuhub.model.user.UserSetEntity;
import org.skuhub.skuhub.repository.users.UserInfoRepository;
import org.skuhub.skuhub.repository.users.UserSetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserSetRepository userSetRepository;
    private final UserInfoRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    @Transactional
    public JwtDto signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.Conflict, "이미 존재하는 회원입니다.", HttpStatus.CONFLICT);
        }

        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        String userId = generateUserIdFromEmail(request.getEmail());

        UserInfoJpaEntity user = UserInfoJpaEntity.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .year(request.getYear())
                .department(request.getDepartment())
                .name(request.getName())
                .userId(userId)  // userId 설정
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return jwtUtil.generateJwtDto(user.getUserId(), "USER");
    }

    @Override
    public JwtDto login(LoginRequest request) {
        // 사용자 조회
        Optional<UserInfoJpaEntity> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            logger.warn("사용자 없음: email={}", request.getEmail());
            throw new CustomException(ErrorCode.NotFound, "사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        UserInfoJpaEntity user = userOptional.get();

        // 비밀번호 확인
        String encodedPassword = passwordEncoder.encode("sang1002!"); // 가입 시 비밀번호 암호화

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {

            throw new CustomException(ErrorCode.Unauthorized, "비밀번호를 확인해 주세요.", HttpStatus.UNAUTHORIZED);
        }
        // JWT 생성
        return jwtUtil.generateJwtDto(user.getUserId(), "USER");
    }


    @Override
    public JwtDto reissue(String refreshToken) {
        if (!jwtUtil.isValidRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.Conflict, "올바른 Refresh 토큰이 아닙니다.", HttpStatus.CONFLICT);
        }
        String userId = jwtUtil.getClaims(refreshToken).getSubject();
        return jwtUtil.generateJwtDto(userId, "USER");
    }

    @Override
    public boolean getEmailValidity(String email) {
        return !userRepository.existsByEmail(email);
    }

    private String generateUserIdFromEmail(String email) {
        return email.split("@")[0]; // 예: email@example.com -> "email"
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        // 이메일로 사용자 조회
        UserSetEntity user = userSetRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 새 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(request.getNewPassword());

        // 비밀번호만 새로 설정한 새 객체 생성
        user = user.toBuilder()
                .password(encryptedPassword) // 비밀번호 변경
                .build();

        // 변경된 비밀번호 저장
        userSetRepository.save(user);
    }

    @Override
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    @Override
    public boolean isPasswordValid(String userId, String password) {
        // userId로 사용자 정보 조회
        Optional<UserInfoJpaEntity> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new CustomException(ErrorCode.NotFound, "사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        UserInfoJpaEntity user = userOptional.get();

        // 비밀번호 일치 여부 확인
        return passwordEncoder.matches(password, user.getPassword());
    }
}
