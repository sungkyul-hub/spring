package org.skuhub.skuhub.api.auth.controller;

import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.api.auth.dto.request.ChangePasswordRequest;
import org.skuhub.skuhub.api.auth.dto.request.LoginRequest;
import org.skuhub.skuhub.api.auth.dto.request.ReissueRequest;
import org.skuhub.skuhub.api.auth.dto.request.SignupRequest;
import org.skuhub.skuhub.api.auth.dto.response.ValidResponse;
import org.skuhub.skuhub.api.auth.service.AuthService;
import org.skuhub.skuhub.api.auth.service.MailService;
import org.skuhub.skuhub.api.user.service.UserService;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.common.utills.jwt.dto.JwtDto;
import org.skuhub.skuhub.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Slf4j
@RestController
@Tag(name = "Auth", description = "Auth API")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MailService mailService;
    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "회원 가입(일반)", description = "일반 회원 가입 API")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public BaseResponse<JwtDto> signup(@RequestBody SignupRequest request) {
        JwtDto jwtDto = authService.signup(request);
        BaseResponse<JwtDto> response = new BaseResponse<>(jwtDto);
        response.setCode("201");
        response.setMessage("회원가입 성공");
        response.setSuccess(true); // success 필드를 true로 설정
        return response;
    }


    @Operation(summary = "로그인(일반)", description = "일반 로그인 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public BaseResponse<JwtDto> login(@RequestBody LoginRequest request) {
        BaseResponse<JwtDto> response = new BaseResponse<>(authService.login(request));
        response.setCode("200");
        response.setMessage("로그인 성공");
        response.setSuccess(true);

        return response;
    }

    @Operation(summary = "이메일 인증번호 발송", description = "이메일 인증번호를 발송하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send-email-verification")
    public BaseResponse<String> sendVerificationCode(@RequestParam String email) {
        if (!email.endsWith("@sungkyul.ac.kr")) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이메일 도메인입니다.", "유효하지 않은 이메일 도메인입니다.",false);
        }

        try {
            String authCode = mailService.sendSimpleMessage(email);
            return new BaseResponse<>(HttpStatus.OK.value(), "메일 발송 성공", authCode);
        } catch (MessagingException e) {
            log.error("메일 발송 오류: {}", e.getMessage(), e);
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "메일 발송 실패", "메일 발송 실패",false);
        }
    }

    @Operation(summary = "비밀번호 변경용 이메일 인증번호 발송", description = "비밀번호 변경을 위한 이메일 인증번호를 발송하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send-password-reset-verification")
    public BaseResponse<String> sendPasswordResetVerificationCode(@RequestParam String email, @RequestParam String userId) {
        // 이메일 도메인 검사
        if (!email.endsWith("@sungkyul.ac.kr")) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이메일 도메인입니다.", "유효하지 않은 이메일 도메인입니다.",false);
        }

        try {
            // 유저 아이디 검사 (예시: DB에서 유저 아이디 존재 여부 확인)
            boolean isUserValid = userService.isUserValid(userId); // 유저 아이디가 유효한지 체크하는 서비스 메서드 호출
            if (!isUserValid) {
                return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "아이디가 올바르지 않습니다.", "유효하지 않은 유저 아이디입니다.",false);
            }

            // 이메일과 유저아이디 검증 후 비밀번호 변경 인증번호 발송
            String authCode = mailService.sendPasswordResetEmail(email); // 비밀번호 변경 인증번호 발송
            return new BaseResponse<>(HttpStatus.OK.value(), "메일 발송 성공", authCode,true);
        } catch (MessagingException e) {
            log.error("메일 발송 오류: {}", e.getMessage(), e);
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "메일 발송 실패", "메일 발송 실패",false);
        } catch (CustomException e) {
            // 유저 아이디가 유효하지 않을 경우 예외 처리
            return new BaseResponse<>(e.getStatus().value(), e.getMessage(), e.getMessage());
        }
    }

    @Operation(summary = "이메일 인증번호 확인", description = "사용자가 입력한 인증번호를 검증하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verfy-email")
    public BaseResponse<String> verifyEmailCode(@RequestParam String email, @RequestParam String code) {
        if (mailService.verifyCode(email, code)) {
            // 인증 성공 시
            return new BaseResponse<String>(HttpStatus.OK.value(),"인증에 성공하였습니다.", "인증에 성공하였습니다.",true);
        } else {
            // 인증 실패 시
            return new BaseResponse<String>(HttpStatus.BAD_REQUEST.value(), "인증에 실패하였습니다.", "인증에 실패하였습니다.",false);
        }
    }

    @Operation(summary = "비밀번호 변경용 이메일 인증번호 확인", description = "비밀번호 변경을 위한 이메일 인증번호를 검증하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/find-password/verify-email")
    public BaseResponse<String> verifyPasswordResetCode(@RequestParam String email, @RequestParam String code) {
        if (mailService.verifyCode(email, code)) {
            return new BaseResponse<>(HttpStatus.OK.value(), "비밀번호 변경 인증에 성공하였습니다.", "비밀번호 변경 인증에 성공하였습니다.",true);
        } else {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "비밀번호 변경 인증에 실패하였습니다.", "비밀번호 변경 인증에 실패하였습니다.",false);
        }
    }

    @Operation(summary = "비밀번호 변경", description = "사용자가 이메일 인증 후 비밀번호를 변경하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/change-password")
    public BaseResponse<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            authService.changePassword(request); // 비밀번호 변경 서비스 호출
            return new BaseResponse<>(HttpStatus.OK.value(), "비밀번호 변경에 성공하였습니다.", "비밀번호 변경에 성공하였습니다.",true);
        } catch (Exception e) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "비밀번호 변경에 실패하였습니다.", e.getMessage(),false);
        }
    }

    @Operation(summary = "이메일 중복여부 검사", description = "이메일 중복여부 검사 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/duplicated")
    public BaseResponse<ValidResponse> getEmailDuplicated(@RequestParam String email) {
        try {
            boolean isValid = authService.getEmailValidity(email);

            // ValidResponse 객체를 생성하여 data로 설정
            return BaseResponse.<ValidResponse>builder()
                    .isSuccess(true)
                    .code("200")
                    .message("이메일 중복여부 검사 완료")
                    .data(ValidResponse.builder().valid(isValid).build())  // ValidResponse 타입을 설정
                    .build();
        } catch (Exception e) {
            // 실패시 error 메시지 전달
            return BaseResponse.<ValidResponse>builder()
                    .isSuccess(false)
                    .code("400")
                    .message("이메일 중복여부 검사 실패")
                    .data(ValidResponse.builder().valid(false).build())  // 실패시 false 반환
                    .build();
        }
    }

    @Operation(summary = "유저 아이디 중복 검사", description = "회원가입 시 유저 아이디의 중복 여부를 확인하는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/duplicate")
    public BaseResponse<Boolean> checkUserIdDuplication(@RequestParam String userId) {
        boolean isAvailable = authService.isUserIdAvailable(userId);
        BaseResponse<Boolean> response = new BaseResponse<>(isAvailable);
        response.setCode("200");
        response.setMessage(isAvailable ? "사용 가능한 아이디입니다." : "이미 사용 중인 아이디입니다.");
        response.setSuccess(true);
        return response;

    }

    @Operation(summary = "비밀번호 일치 여부 확인", description = "사용자가 입력한 비밀번호가 일치하는지 확인하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify-password")
    public BaseResponse<String> verifyPassword(@RequestParam String userId, @RequestParam String password) {
        boolean isValid = authService.isPasswordValid(userId, password);
        if (isValid) {
            return new BaseResponse<>(HttpStatus.OK.value(), "비밀번호가 일치합니다.", "비밀번호가 일치합니다.",true);
        } else {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.", "비밀번호가 일치하지 않습니다.",false);
        }
    }

    @Operation(summary = "토큰 재발급", description = "리프레쉬 토큰 기반 액세스 토큰을 재발급 받는 API")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/reissue")
    public BaseResponse<JwtDto> reissue(@RequestBody ReissueRequest request) {
        return new BaseResponse<>(authService.reissue(request.getRefreshToken()));
    }
    
}



