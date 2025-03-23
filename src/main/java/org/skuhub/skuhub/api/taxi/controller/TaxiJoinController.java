package org.skuhub.skuhub.api.taxi.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiJoinRequest;
import org.skuhub.skuhub.api.taxi.service.TaxiJoinServiceImpl;
import org.skuhub.skuhub.api.taxi.service.TaxiPostServiceImpl;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/taxi/join")
@Slf4j
public class TaxiJoinController {
    private final JWTUtil jwtUtil;
    private final TaxiJoinServiceImpl taxiJoinServiceImpl;

    @Operation(summary = "택시합승 참여", description = "택시합승 게시글에 참여하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public BaseResponse<String> joinTaxiShare(@RequestBody TaxiJoinRequest taxiJoinRequest, HttpServletRequest request) throws IOException {
        String userId = jwtUtil.getUserId(request);
        return taxiJoinServiceImpl.joinTaxiShare(taxiJoinRequest, userId);
    }

    @Operation(summary = "택시합승 참여 취소", description = "택시합승 게시글에 참여 취소하는 API")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("")
    public BaseResponse<String> leaveTaxiShare(@RequestBody TaxiJoinRequest taxiJoinRequest, HttpServletRequest request) {
        String userId = jwtUtil.getUserId(request);
        return taxiJoinServiceImpl.leaveTaxiShare(taxiJoinRequest, userId);
    }
}
