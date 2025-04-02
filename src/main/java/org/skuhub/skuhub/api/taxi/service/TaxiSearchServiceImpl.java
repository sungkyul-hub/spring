package org.skuhub.skuhub.api.taxi.service;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiJoinRequest;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiPostResponse;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.skuhub.skuhub.exceptions.CustomException;
import org.skuhub.skuhub.model.taxi.TaxiJoinJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.skuhub.skuhub.repository.taxi.TaxiJoinRepository;
import org.skuhub.skuhub.repository.taxi.TaxiShareRepository;
import org.skuhub.skuhub.repository.users.UserInfoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Service
@Slf4j
public class TaxiSearchServiceImpl implements TaxiSearchService {
    private final TaxiShareRepository taxiShareRepository;

    public TaxiSearchServiceImpl(TaxiShareRepository taxiShareRepository) {
        this.taxiShareRepository = taxiShareRepository;
    }

    @Override
    public BaseResponse<List<TaxiPostResponse>> searchTaxiShare(String keyword, Long cursor, int limit) {
        List<TaxiShareJpaEntity> lastPostId = taxiShareRepository.findLastPostId();
        long cursorValue = cursor != null ? cursor : lastPostId.getFirst().getPostId() + 1;
        log.info(String.valueOf(lastPostId.getFirst().getPostId()));
        Pageable pageable = PageRequest.of(0, limit);
        List<TaxiPostResponse> taxiShareJpaEntities = taxiShareRepository.findByTitle(keyword, cursorValue, pageable).stream().map(taxiShare -> {
            TaxiPostResponse response = new TaxiPostResponse();
            response.setPostId(taxiShare.getPostId());
            response.setName(taxiShare.getUserKey().getName()); // userId 설정
            response.setTitle(taxiShare.getTitle());
            response.setDepartureLocation(taxiShare.getDepartureLocation());
            response.setRideTime(taxiShare.getRideTime());
            response.setDescription(taxiShare.getDescription());
            response.setHeadCount(taxiShare.getHeadCount());
            response.setNumberOfPeople(taxiShare.getNumberOfPeople());
            LocalDateTime createdAt = taxiShare.getCreatedAt();
            OffsetDateTime offsetCreatedAt = createdAt.atOffset(ZoneOffset.UTC);
            response.setCreatedAt(offsetCreatedAt);
            return response;
        }).collect(Collectors.toList());

        if(taxiShareJpaEntities.isEmpty()) {
            return new BaseResponse<>(false, "404", "택시합승 게시글이 존재하지 않습니다.", OffsetDateTime.now(), null);
        }


        return new BaseResponse<>(true, "200", "택시합승 게시글 조회 성공", OffsetDateTime.now(), taxiShareJpaEntities);
    }
}
