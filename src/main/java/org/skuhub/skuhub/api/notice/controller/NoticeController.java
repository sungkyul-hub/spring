package org.skuhub.skuhub.api.notice.controller;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skuhub.skuhub.api.notice.dto.response.NoticeDetailsResponse;
import org.skuhub.skuhub.api.notice.dto.response.NoticeResponse;
import org.skuhub.skuhub.api.notice.service.NoticeServiceImpl;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.skuhub.skuhub.common.response.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/notice")
@Slf4j
public class NoticeController {
    private final NoticeServiceImpl noticeServiceImpl;
    private final JWTUtil jwtUtil;

    @Operation(summary = "공지사항 검색", description = "공지사항을 검색하는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public BaseResponse<List<NoticeResponse>> searchNotice(
            @RequestParam String keyword,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int limit) {

        log.info("keyword: {}, cursor: {}, limit: {}", keyword, cursor, limit);
        return noticeServiceImpl.searchNotice(keyword, cursor, limit);
    }


    @Operation(summary = "공지사항 카테고리 검색", description = "공지사항의 카테고리를 검색하는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/category/{category}")
    public BaseResponse<List<NoticeResponse>> categoryNotice(
            @PathVariable String category,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("category: {}, cursor: {}, limit: {}", category, cursor, limit);
        return noticeServiceImpl.categoryNotice(category,  cursor, limit);
    }

    @Operation(summary = "공지 상세보기", description = "공지를 상세하게 보는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/contents/{noticeId}")
    public BaseResponse<NoticeDetailsResponse> detailNotice(@PathVariable Long noticeId) {
        log.info("noticeId: {}", noticeId);
        return noticeServiceImpl.detailNotice(noticeId);
    }

    @Operation(summary = "공지 히스토리", description = "공지 히스토리 보는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/history")
    public BaseResponse<List<NoticeResponse>> noticeHistory(HttpServletRequest request) {
        String userId = jwtUtil.getUserId(request);
        return noticeServiceImpl.noticeHistory(userId);
    }
}
