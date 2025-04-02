package org.skuhub.skuhub.api.taxi.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.skuhub.skuhub.api.push.controller.PushController;
import org.skuhub.skuhub.api.push.service.PushServiceImpl;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentDeleteRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentEditRequest;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentRequest;
import org.skuhub.skuhub.api.taxi.dto.response.TaxiCommentResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Getter
@Service
@Slf4j
public class TaxiCommentServiceImpl implements TaxiCommentService{
    private final TaxiShareRepository taxiShareRepository;
    private final UserInfoRepository userInfoRepository;
    private final TaxiCommentRepository taxiCommentRepository;
    private final PushServiceImpl pushServiceImpl;

    public TaxiCommentServiceImpl(TaxiShareRepository taxiShareRepository, UserInfoRepository userInfoRepository, TaxiCommentRepository taxiCommentRepository, PushServiceImpl pushServiceImpl) {
        this.taxiShareRepository = taxiShareRepository;
        this.userInfoRepository = userInfoRepository;
        this.taxiCommentRepository = taxiCommentRepository;
        this.pushServiceImpl = pushServiceImpl;
    }

    public BaseResponse<String> postTaxiComment(TaxiCommentRequest request, String userId) throws IOException {
        // userId로 사용자 정보를 찾기
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // TaxiShareJpaEntity 조회
        TaxiShareJpaEntity post = taxiShareRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        // 사용자 이름을 가져오기
        String userName = userEntity.getName();

        TaxiCommentJpaEntity entity = new TaxiCommentJpaEntity();
        entity.setPostId(post);
        entity.setUserKey(userEntity);
        entity.setCommentContent(request.getCommentContent());
        entity.setCreatedAt(OffsetDateTime.now());


        if(userName.equals(request.getName())) {
            TaxiCommentJpaEntity saved =  taxiCommentRepository.save(entity);
            boolean push = pushServiceImpl.pushTaxiCommentAlarm(post.getPostId(), request.getCommentContent());
            return new BaseResponse<>(true, "201", "택시합승 댓글 저장 성공", OffsetDateTime.now(), "댓글 작성 성공");
        }else {
            throw new CustomException(ErrorCode.BadRequest, "수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BaseResponse<List<TaxiCommentResponse>> getTaxiComment(Long postId) {
        TaxiShareJpaEntity post = taxiShareRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 댓글 조회
        List<TaxiCommentResponse> comments = taxiCommentRepository.findByPostId(post).stream()
                .sorted(Comparator.comparing(TaxiCommentJpaEntity::getCreatedAt))
                .map(comment -> {
                    TaxiCommentResponse response = new TaxiCommentResponse();
                    response.setCommentId(comment.getCommentId());
                    response.setName(comment.getUserKey().getName());
                    response.setCommentContent(comment.getCommentContent());
                    response.setCreatedAt(comment.getCreatedAt());
                    return response;
                }).toList();

        if(comments.isEmpty()) {
            throw new CustomException(ErrorCode.NotFound, "댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        return new BaseResponse<>(true, "200", "택시합승 댓글 조회 성공", OffsetDateTime.now(), comments);
    }

    @Override
    public BaseResponse<String> editTaxiComment(TaxiCommentEditRequest request, String userId) {
        TaxiShareJpaEntity post = taxiShareRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        TaxiCommentJpaEntity comment = taxiCommentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        if(comment.getUserKey().getUserId().equals(userId)) {
            comment.setCommentContent(request.getCommentContent());
            taxiCommentRepository.save(comment);
            return new BaseResponse<>(true, "200", "택시합승 댓글 수정 성공", OffsetDateTime.now(), "댓글 수정 성공");
        }else {
            return new BaseResponse<>(false, "403", "수정 권한이 없습니다.", OffsetDateTime.now(), "수정 권한이 없습니다.");
        }
    }

    @Override
    public BaseResponse<String> deleteTaxiComment(TaxiCommentDeleteRequest request, String userId) {
        TaxiShareJpaEntity post = taxiShareRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        TaxiCommentJpaEntity comment = taxiCommentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        log.info("게시글 ID: {}", request.getPostId());

        if(comment.getUserKey().getUserId().equals(userId)) {
            taxiCommentRepository.deleteById(request.getCommentId());
            return new BaseResponse<>(true, "200", "택시합승 댓글 삭제 성공", OffsetDateTime.now(), "댓글 삭제 성공");
        }else {
            return new BaseResponse<>(false, "403", "삭제 권한이 없습니다.", OffsetDateTime.now(), "삭제 권한이 없습니다.");
        }
    }

}
