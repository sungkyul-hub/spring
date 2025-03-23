package org.skuhub.skuhub.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.api.user.dto.request.UpdateUserRequest;
import org.skuhub.skuhub.api.user.dto.request.UserIdRequest;
import org.skuhub.skuhub.api.user.dto.response.UpdatedUserInfoResponse;
import org.skuhub.skuhub.api.user.service.UserService;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;
import org.skuhub.skuhub.api.user.dto.response.UserInfoResponse;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;


@RestController
@Tag(name = "User", description = "User API")
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "유저 아이디 찾기", description = "이메일을 통해 유저 아이디를 찾는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/find-username")
    public BaseResponse<String> findUserId(@RequestBody UserIdRequest request) {
        // 이메일 형식 검사
        if (!request.getEmail().endsWith("@sungkyul.ac.kr")) {
            return new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이메일 도메인입니다.", "유효하지 않은 이메일 도메인입니다.");
        }

        // 이메일로 유저 아이디 찾기
        String userId = userService.findUserIdByEmail(request.getEmail());

        // 유저가 존재하지 않는 경우 처리
        if (userId == null) {
            return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "이메일에 해당하는 유저가 없습니다.", "이메일에 해당하는 유저가 없습니다.");
        }

        // 성공적인 유저 아이디 반환
        return new BaseResponse<>(HttpStatus.OK.value(), "성공적으로 유저 아이디를 찾았습니다.", userId);
    }

    @Operation(summary = "사용자 정보 변경", description = "학과, 비밀번호, 닉네임을 변경하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/profile/modify")
    public ResponseEntity<BaseResponse<UpdatedUserInfoResponse>> updateUser(HttpServletRequest request, @RequestBody UpdateUserRequest updateUserRequest) {
        String userId = jwtUtil.getUserId(request);

        try {
            UpdatedUserInfoResponse updatedUserInfoResponse = userService.updateUserInfo(userId, updateUserRequest);
            return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "사용자 정보가 성공적으로 변경되었습니다.", updatedUserInfoResponse));
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.NotFound, e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "사용자 정보 가져오기", description = "사용자 정보 가져오는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/info")
    public ResponseEntity<BaseResponse<UserInfoResponse>> getUserInfo(HttpServletRequest request) {
        String userId = jwtUtil.getUserId(request);
        UserInfoResponse userInfo = userService.getUserInfo(userId);

        return ResponseEntity.ok(new BaseResponse<>(200, "사용자 정보 조회 성공", userInfo));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API")
    @DeleteMapping("/withdraw")
    public ResponseEntity<BaseResponse<String>> withdrawUser(HttpServletRequest request) {
        String userId = jwtUtil.getUserId(request); // JWT에서 userId 추출
        userService.withdrawUser(userId); // 회원 삭제 처리
        return ResponseEntity.ok(new BaseResponse<>(200, "회원 탈퇴 성공", "회원 탈퇴가 완료되었습니다."));
    }

}
