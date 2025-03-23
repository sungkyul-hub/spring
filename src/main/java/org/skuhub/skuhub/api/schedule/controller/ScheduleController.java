package org.skuhub.skuhub.api.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.skuhub.skuhub.api.schedule.service.ScheduleService;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.model.schedule.ScheduleEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(
            summary = "특정 날짜의 학사 일정 조회",
            description = "쿼리 파라미터로 전달된 date (YYYY-MM-DD)를 기준으로, 해당 날짜가 일정의 시작과 종료 사이에 포함되는 학사 일정 목록을 조회합니다."
    )
    @GetMapping("/contents")
    public ResponseEntity<BaseResponse<List<ScheduleEntity>>> getSchedulesByDate(
            @RequestParam("date") String dateStr
    ) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>(400, "날짜 형식이 올바르지 않습니다. (YYYY-MM-DD)", null, false)
            );
        }

        Instant target = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();

        List<ScheduleEntity> schedules = scheduleService.getSchedulesByTargetDate(target);
        if (schedules.isEmpty()) {
            return ResponseEntity.status(404).body(
                    new BaseResponse<>(404, "선택한 날짜의 학사 일정이 존재하지 않습니다.", null, false)
            );
        }
        return ResponseEntity.ok(
                new BaseResponse<>(200, "선택한 날짜의 학사 일정 조회 성공", schedules, true)
        );
    }
}
