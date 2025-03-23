package org.skuhub.skuhub.api.taxi.controller;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiEditRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiPostDeleteRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiPostRequest;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiPostResponse;
import org.skuhub.skuhub.api.taxi.service.TaxiPostServiceImpl;
import org.skuhub.skuhub.api.taxi.service.TaxiSearchServiceImpl;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.skuhub.skuhub.common.response.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/taxi/search")
@Slf4j
public class TaxiSearchController {

    private final TaxiSearchServiceImpl taxiSearchServiceImpl;

    @Operation(summary = "택시합승 검색", description = "택시합승 게시글을 검색하는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public BaseResponse<List<TaxiPostResponse>> searchTaxiShare(
            @RequestParam String keyword,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("keyword: {}, cursor: {}, limit: {}", keyword, cursor, limit);
        return taxiSearchServiceImpl.searchTaxiShare(keyword, cursor, limit);
    }
}
