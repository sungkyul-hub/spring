package org.skuhub.skuhub.api.timetable.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.api.timetable.dto.request.CompletionRequest;
import org.skuhub.skuhub.api.timetable.dto.request.DeleteUserTimetableRequest;
import org.skuhub.skuhub.api.timetable.dto.request.ScoreRequest;
import org.skuhub.skuhub.api.timetable.dto.request.UpdateScoreRequest;
import org.skuhub.skuhub.api.timetable.dto.request.UpdateUserTimetableRequest;
import org.skuhub.skuhub.api.timetable.dto.request.UserTimetableRequest;
import org.skuhub.skuhub.api.timetable.dto.response.CompletionResponse;
import org.skuhub.skuhub.api.timetable.dto.response.PersonalTimetableResponse;
import org.skuhub.skuhub.api.timetable.dto.response.TimetableScheduleResponse;
import org.skuhub.skuhub.api.timetable.dto.response.UserTimetableResponse;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.model.timetable.CompletionEntity;
import org.skuhub.skuhub.model.timetable.PersonalTimetableEntity;
import org.skuhub.skuhub.api.timetable.service.TimetableService;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.skuhub.skuhub.model.timetable.TimetableScheduleEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/timetable")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;
    private final JWTUtil jwtUtil;


    @Operation(summary = "전체 시간표 조회", description = "전체 강의 스케줄을 조회합니다.")
    @GetMapping("/schedule/class")
    public ResponseEntity<?> getAllSchedules() {
        List<TimetableScheduleEntity> entities = timetableService.getAllSchedules();

        if (entities.isEmpty()) {
            return ResponseEntity.status(404).body(
                    Map.of("code", 404, "message", "시간표를 찾을 수 없습니다.", "result", null, "isSuccess", false)
            );
        }

        List<TimetableScheduleResponse> responseList = entities.stream()
                .map(TimetableScheduleResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(
                Map.of("code", 200, "message", "전체 시간표를 성공적으로 가져왔습니다.", "result", responseList)
        );
    }

    @Operation(summary = "조건 검색 시간표 조회", description = "주어진 조건에 따라 시간표를 검색합니다.")
    @GetMapping("/schedule/search")
    public ResponseEntity<?> searchTimetable(
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "semester", required = false) Integer semester,
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "grade", required = false) Integer grade,
            @RequestParam(name = "subjectCode", required = false) Integer subjectCode,
            @RequestParam(name = "subjectName", required = false) String subjectName,
            @RequestParam(name = "professorName", required = false) String professorName,
            @RequestParam(name = "classTime", required = false) String classTime,
            @RequestParam(name = "classroom", required = false) String classroom
    ) {
        List<TimetableScheduleEntity> result = timetableService.findSchedulesByConditions(
                year, semester, department, grade, subjectCode, subjectName, professorName, classTime, classroom
        );
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body(
                    Map.of("code", 404, "message", "시간표를 찾을 수 없습니다.")
            );
        }
        return ResponseEntity.ok(
                Map.of("isSuccess", true, "code", 200, "message", "success!", "result", result)
        );
    }


    @Operation(summary = "사용자 개인 시간표 조회", description = "JWT 토큰으로 인증된 사용자의 개인 시간표를 조회합니다.")
    @GetMapping("/user/time")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<List<PersonalTimetableResponse>>> getUserPersonalTimetable(HttpServletRequest request) {
        // JWT 토큰에서 userId 추출 후 userKey 조회
        String userIdStr = jwtUtil.getUserId(request);
        Integer userKey = timetableService.findUserKeyByUserId(userIdStr);
        List<PersonalTimetableEntity> timetable = timetableService.getUserPersonalTimetable(userKey);
        if (timetable.isEmpty()) {
            return ResponseEntity.status(404).body(
                    new BaseResponse<>(404, "개인 시간표를 찾을 수 없습니다.", null, false)
            );
        }
        List<PersonalTimetableResponse> responseList = timetable.stream()
                .map(PersonalTimetableResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(
                new BaseResponse<>(200, "사용자의 시간표를 성공적으로 가져왔습니다.", responseList, true)
        );
    }


    @Operation(summary = "사용자 시간표 등록", description = "JWT 토큰으로 인증된 사용자의 시간표를 등록합니다.")
    @PostMapping("/user/post")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<?>> createUserTimetable(HttpServletRequest request,
                                                               @RequestBody UserTimetableRequest req) {

        String userIdStr = jwtUtil.getUserId(request);
        Integer userKey = timetableService.findUserKeyByUserId(userIdStr);
        req.setUserKey(userKey);
        timetableService.createUserTimetable(req);
        return ResponseEntity.status(201).body(
                new BaseResponse<>(201, "시간표를 성공적으로 등록했습니다.", req.getTimetableId(), true)
        );
    }

    @Operation(summary = "사용자 시간표 조회", description = "JWT 토큰으로 인증된 사용자의 시간표를 조회합니다.")
    @GetMapping("/user_time")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<List<UserTimetableResponse>>> getUserTimetable(HttpServletRequest request) {
        String userIdStr = jwtUtil.getUserId(request);
        Integer userKey = timetableService.findUserKeyByUserId(userIdStr);
        List<UserTimetableResponse> responseList = timetableService.getUserTimetable(userKey);
        if (responseList.isEmpty()) {
            return ResponseEntity.status(404).body(
                    new BaseResponse<>(404, "개인 시간표를 찾을 수 없습니다.", null, false)
            );
        }
        return ResponseEntity.ok(new BaseResponse<>(200, "사용자의 시간표를 성공적으로 가져왔습니다.", responseList, true));
    }

    @Operation(summary = "사용자 시간표 수정", description = "JWT 토큰으로 인증된 사용자의 시간표를 수정합니다.")
    @PostMapping("/user/edit")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<?>> editUserTimetable(HttpServletRequest request,
                                                             @RequestBody UpdateUserTimetableRequest req) {
        if (req.getPersonalKey() == null) {
            return ResponseEntity.status(400).body(
                    new BaseResponse<>(400, "Error: Personal Key is missing! Please provide a valid personal_key.", null, false)
            );
        }
        String userIdStr = jwtUtil.getUserId(request);
        Integer userKey = timetableService.findUserKeyByUserId(userIdStr);
        req.setUserKey(userKey);
        boolean isUpdated = timetableService.updateUserTimetable(req);
        if (!isUpdated) {
            return ResponseEntity.status(400).body(
                    new BaseResponse<>(400, "필수 값이 입력되지 않았습니다.", null, false)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse<>(201, "시간표를 성공적으로 수정했습니다.", req.getTimetableId(), true)
        );
    }

    @Operation(summary = "사용자 시간표 삭제", description = "JWT 토큰으로 인증된 사용자의 시간표를 삭제합니다.")
    @DeleteMapping("/user/delete")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<?>> deleteUserTimetable(HttpServletRequest request,
                                                               @RequestBody DeleteUserTimetableRequest req) {
        if (req.getPersonalKey() == null) {
            return ResponseEntity.status(400).body(
                    new BaseResponse<>(400, "Error: Personal Key is missing! Please provide a valid personal_key.", null, false)
            );
        }

        boolean isDeleted = timetableService.deleteUserTimetable(req.getPersonalKey());
        if (!isDeleted) {
            return ResponseEntity.status(404).body(
                    new BaseResponse<>(404, "시간표를 찾을 수 없습니다.", null, false)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse<>(200, "시간표를 성공적으로 삭제했습니다.", null, true)
        );
    }

    @Operation(summary = "사용자 성적 입력", description = "JWT 토큰으로 인증된 사용자의 시간표에 성적을 입력합니다.")
    @PostMapping("/score/post")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<?>> addScore(HttpServletRequest request,
                                                    @RequestBody ScoreRequest req) {

        String userIdStr = jwtUtil.getUserId(request);
        Integer userKey = timetableService.findUserKeyByUserId(userIdStr);
        req.setUserKey(userKey);
        boolean isUpdated = timetableService.addScoreToUserTimetable(
                req.getPersonalKey(), req.getScore(), req.getMajorStatus()
        );
        if (!isUpdated) {
            return ResponseEntity.status(404).body(
                    new BaseResponse<>(404, "시간표를 찾을 수 없습니다.", null, false)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse<>(201, "성적을 성공적으로 입력했습니다.", req.getPersonalKey(), true)
        );
    }

    @Operation(summary = "사용자 성적 수정", description = "JWT 토큰으로 인증된 사용자의 시간표의 성적을 수정합니다.")
    @PostMapping("/score/edit")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<?>> editScore(HttpServletRequest request,
                                                     @RequestBody UpdateScoreRequest req) {
        if (req.getPersonalKey() == null || req.getScore() == null) {
            return ResponseEntity.status(400).body(
                    new BaseResponse<>(400, "필수 값이 입력되지 않았습니다.", null, false)
            );
        }

        boolean isUpdated = timetableService.updateUserScore(req);
        if (!isUpdated) {
            return ResponseEntity.status(404).body(
                    new BaseResponse<>(404, "시간표를 찾을 수 없습니다.", null, false)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse<>(201, "성적을 성공적으로 수정했습니다.", req.getPersonalKey(), true)
        );
    }


    @Operation(summary = "이수구분 정보 입력", description = "JWT 토큰으로 인증된 사용자의 이수구분 정보를 등록합니다.")
    @PostMapping("/completion/post")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<Map<String, Object>>> postCompletion(HttpServletRequest request,
                                                                            @RequestBody CompletionRequest req) {
        if (req.getEarnedCredits() == null || req.getGraduationCredits() == null) {
            return ResponseEntity.status(400).body(
                    new BaseResponse<>(400, "필수 값이 입력되지 않았습니다.", null, false)
            );
        }
        String userIdStr = jwtUtil.getUserId(request);
        Integer userKeyint = timetableService.findUserKeyByUserId(userIdStr);
        Long userKey = userKeyint.longValue();
        req.setUserKey(userKey);
        CompletionEntity savedData = timetableService.saveCompletionData(req);
        Map<String, Object> result = new HashMap<>();
        result.put("user_key", savedData.getUserInfoTb().getUserKey());
        result.put("updated_at", DateTimeFormatter.ISO_INSTANT.format(savedData.getUpdatedAt()));
        return ResponseEntity.ok(new BaseResponse<>(201, "이수구분 정보가 성공적으로 입력되었습니다.", result, true));
    }

    @Operation(summary = "이수구분 정보 수정", description = "JWT 토큰으로 인증된 사용자의 이수구분 정보를 수정합니다.")
    @PostMapping("/completion/edit")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseResponse<CompletionResponse>> editCompletion(HttpServletRequest request,
                                                                           @RequestBody CompletionRequest req) {
        String userIdStr = jwtUtil.getUserId(request);
        Integer userKeyint = timetableService.findUserKeyByUserId(userIdStr);
        Long userKey = userKeyint.longValue();
        req.setUserKey(userKey);
        CompletionEntity completion = timetableService.updateCompletionData(req);
        CompletionResponse response = new CompletionResponse(
                completion.getUserInfoTb().getUserKey(),
                completion.getUpdatedAt().toInstant()
        );

        return ResponseEntity.status(201).body(
                new BaseResponse<>(201, "이수구분 정보가 성공적으로 수정되었습니다.", response, true)
        );
    }
}