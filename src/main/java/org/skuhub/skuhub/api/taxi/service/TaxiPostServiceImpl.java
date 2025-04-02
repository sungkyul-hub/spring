package org.skuhub.skuhub.api.taxi.service;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertySource;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiEditRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiPostDeleteRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiPostRequest;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiCommentResponse;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiPostDetailsResponse;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiPostResponse;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.common.utills.jwt.JWTUtil;
import org.skuhub.skuhub.exceptions.CustomException;
import org.skuhub.skuhub.model.taxi.TaxiCommentJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.skuhub.skuhub.repository.taxi.TaxiCommentRepository;
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
import java.util.stream.Collectors;
import java.util.Comparator;


import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Getter
@Service
@Slf4j
public class TaxiPostServiceImpl implements TaxiPostService {

    private final TaxiShareRepository taxiShareRepository;
    private final UserInfoRepository userInfoRepository;
    private final TaxiCommentRepository taxiCommentRepository;

    public TaxiPostServiceImpl(TaxiShareRepository taxiShareRepository, UserInfoRepository userInfoRepository, TaxiCommentRepository taxiCommentRepository) {
        this.taxiShareRepository = taxiShareRepository;
        this.userInfoRepository = userInfoRepository;
        this.taxiCommentRepository = taxiCommentRepository;
    }

    public BaseResponse<String> postTaxiShare(TaxiPostRequest request, String userId) {


    UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND));

    TaxiShareJpaEntity entity = new TaxiShareJpaEntity();
    entity.setUserKey(userEntity);
    entity.setTitle(request.getTitle());
    entity.setDepartureLocation(request.getDepartureLocation());
    entity.setNumberOfPeople(request.getNumberOfPeople());
    entity.setRideTime(request.getRideTime());
    entity.setDescription(request.getDescription());
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());

    try {
        TaxiShareJpaEntity saved = taxiShareRepository.save(entity);
        return new BaseResponse<>(true, "201", "택시합승 게시글 저장 성공", OffsetDateTime.now(), "게시글 작성 성공");
    } catch (Exception e) {
        return new BaseResponse<>(false, "500", "게시글 저장 실패", OffsetDateTime.now(), e.getMessage());
        }
    }

    //택시합승 게시글들을 조회하는 API
    public BaseResponse<List<TaxiPostResponse>> getTaxiShare(Long cursor, int limit) {
        List<TaxiShareJpaEntity> lastPostId = taxiShareRepository.findLastPostId();
        long cursorValue = cursor != null ? cursor : lastPostId.getFirst().getPostId() + 1;
        log.info(String.valueOf(lastPostId.getFirst().getPostId()));
        Pageable pageable = PageRequest.of(0, limit);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<TaxiPostResponse> taxiShares = taxiShareRepository.findAllByCreatedAtToday(startOfDay, endOfDay, cursorValue, pageable).stream().map(taxiShare -> {
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
        }).collect(Collectors.toList()).reversed();

        if(taxiShares.isEmpty()) {
            return new BaseResponse<>(false, "404", "택시합승 게시글이 존재하지 않습니다.", OffsetDateTime.now(), null);
        }


        return new BaseResponse<>(true, "200", "택시합승 게시글 조회 성공", OffsetDateTime.now(), taxiShares);
    }

    public BaseResponse<String> postEditTaxiShare(TaxiEditRequest request, String userId) {

        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        TaxiShareJpaEntity postEntity = taxiShareRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        if(!postEntity.getUserKey().getUserId().equals(userId)) {
            return new BaseResponse<>(false, "403", "수정 권한이 없습니다.", OffsetDateTime.now(), "수정 권한이 없습니다.");
        }else{
            TaxiShareJpaEntity entity = new TaxiShareJpaEntity();
            entity.setUserKey(userEntity);
            entity.setTitle(request.getTitle());
            entity.setDepartureLocation(request.getDepartureLocation());
            entity.setNumberOfPeople(request.getNumberOfPeople());
            entity.setRideTime(request.getRideTime());
            entity.setDescription(request.getDescription());
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setCreatedAt(postEntity.getCreatedAt());
            try {
                TaxiShareJpaEntity saved = taxiShareRepository.save(entity);
                return new BaseResponse<>(true, "200", "택시합승 게시글 수정 성공", OffsetDateTime.now(), "게시글 수정 성공");
            } catch (Exception e) {
                return new BaseResponse<>(false, "500", "게시글 수정 실패", OffsetDateTime.now(), e.getMessage());
            }
        }
    }

    public BaseResponse<String> deleteTaxiShare(TaxiPostDeleteRequest request, String userId) {

        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자가 존재하지 않습니다."));

        TaxiShareJpaEntity postEntity = taxiShareRepository.findById(request.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        if(postEntity.getUserKey() == null) {
            return new BaseResponse<>(false, "404", "게시글이 존재하지 않습니다.", OffsetDateTime.now(), "게시글이 존재하지 않습니다.");
        }

        if(!postEntity.getUserKey().getUserId().equals(userEntity.getUserId())) {
            return new BaseResponse<>(false, "403", "삭제 권한이 없습니다.", OffsetDateTime.now(), "삭제 권한이 없습니다.");
        }

        try {
            taxiShareRepository.deleteById(request.getPostId());
            return new BaseResponse<>(true, "200", "택시합승 게시글 삭제 성공", OffsetDateTime.now(), "게시글 삭제 성공");
        } catch (Exception e) {
            return new BaseResponse<>(false, "500", "게시글 삭제 실패", OffsetDateTime.now(), e.getMessage());
        }
    }

    public BaseResponse<TaxiPostDetailsResponse> getTaxiShareDetail(Long postId) {

        TaxiShareJpaEntity postEntity = taxiShareRepository.findPostWithComments(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND));
        TaxiPostDetailsResponse response = new TaxiPostDetailsResponse();
        response.setPostId(postEntity.getPostId());
        response.setName(postEntity.getUserKey().getName()); // userId 설정
        response.setTitle(postEntity.getTitle());
        response.setDepartureLocation(postEntity.getDepartureLocation());
        response.setRideTime(postEntity.getRideTime());
        response.setDescription(postEntity.getDescription());
        response.setHeadCount(postEntity.getHeadCount());
        response.setNumberOfPeople(postEntity.getNumberOfPeople());
        LocalDateTime createdAt = postEntity.getCreatedAt();
        OffsetDateTime offsetCreatedAt = createdAt.atOffset(ZoneOffset.UTC);
        response.setCreatedAt(offsetCreatedAt);
        response.setComments(postEntity.getCommentTbs().stream()
                .sorted(Comparator.comparing(TaxiCommentJpaEntity::getCreatedAt))
                .map(comment -> {
            TaxiCommentResponse commentResponse = new TaxiCommentResponse();
            commentResponse.setCommentId(comment.getCommentId());
            commentResponse.setName(comment.getUserKey().getName());
            commentResponse.setCommentContent(comment.getCommentContent());
            commentResponse.setCreatedAt(comment.getCreatedAt());
            return commentResponse;
        }).collect(Collectors.toList()));

        return new BaseResponse<>(true, "200", "택시합승 게시글 상세 조회 성공", OffsetDateTime.now(), response);

    }
}
