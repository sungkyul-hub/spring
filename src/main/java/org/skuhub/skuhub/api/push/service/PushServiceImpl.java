package org.skuhub.skuhub.api.push.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.skuhub.skuhub.api.push.dto.KeywordRequest;
import org.skuhub.skuhub.api.push.dto.KeywordResponse;
import org.skuhub.skuhub.api.push.dto.PushRequest;
import org.skuhub.skuhub.common.enums.alarm.PushType;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.exceptions.CustomException;
import org.skuhub.skuhub.external.firebase.fcm.utils.FirebaseUtil;
import org.skuhub.skuhub.model.notice.NoticeJpaEntity;
import org.skuhub.skuhub.model.notice.NotificationHistoryJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiJoinJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;
import org.skuhub.skuhub.model.user.KeywordInfoJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.skuhub.skuhub.repository.notice.NoticeRepository;
import org.skuhub.skuhub.repository.notice.NotificationHistoryRepository;
import org.skuhub.skuhub.repository.taxi.TaxiJoinRepository;
import org.skuhub.skuhub.repository.taxi.TaxiShareRepository;
import org.skuhub.skuhub.repository.users.KeywordInfoRepository;
import org.skuhub.skuhub.repository.users.UserInfoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Getter
@Service
@Slf4j
public class PushServiceImpl implements PushService {
    private final FirebaseUtil firebaseUtil;
    private final UserInfoRepository userInfoRepository;
    private final TaxiShareRepository taxiShareRepository;
    private final TaxiJoinRepository taxiJoinRepository;
    private final NoticeRepository noticeRepository;
    private final KeywordInfoRepository keywordInfoRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;

    public PushServiceImpl(FirebaseUtil firebaseUtil, UserInfoRepository userInfoRepository, TaxiShareRepository taxiShareRepository, TaxiJoinRepository taxiJoinRepository, NoticeRepository noticeRepository, KeywordInfoRepository keywordInfoRepository, NotificationHistoryRepository notificationHistoryRepository) {
        this.firebaseUtil = firebaseUtil;
        this.userInfoRepository = userInfoRepository;
        this.taxiShareRepository = taxiShareRepository;
        this.taxiJoinRepository = taxiJoinRepository;
        this.noticeRepository = noticeRepository;
        this.keywordInfoRepository = keywordInfoRepository;
        this.notificationHistoryRepository = notificationHistoryRepository;
    }

    @Override
    public BaseResponse<String> saveToken(String userId, String tokenRequest) { //fcm token 저장
        log.info("saveToken: userId: {}, tokenRequest: {}", userId, tokenRequest);
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        userEntity.setAccessToken(tokenRequest);
        userInfoRepository.save(userEntity);
        return new BaseResponse<>(true, "200", "토큰 저장 성공", null, "토큰 저장 성공");
    }

    @Override
    public BaseResponse<String> deleteToken(String userId) { //fcm token 삭제
        log.info("saveToken: userId: {}", userId);
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        userEntity.setAccessToken(null);
        userInfoRepository.save(userEntity);
        return new BaseResponse<>(true, "200", "토큰 삭제 성공", null, "토큰 삭제 성공");
    }

    @Override
    public BaseResponse<String> saveKeyword(String userId, String Keyword) {
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        Optional<KeywordInfoJpaEntity> duplication = keywordInfoRepository.findByUserKeyAndKeyword(userEntity, Keyword);
        if (duplication.isPresent()) {
            return new BaseResponse<>(false, "409", "키워드 중복", OffsetDateTime.now(), "키워드 중복");
        }

        KeywordInfoJpaEntity keywordEntity = new KeywordInfoJpaEntity();
        keywordEntity.setUserKey(userEntity);
        keywordEntity.setKeyword(Keyword);
        keywordEntity.setCreatedAt(LocalDateTime.now());
        keywordInfoRepository.save(keywordEntity);
        return new BaseResponse<>(true, "201", "키워드 저장 성공", OffsetDateTime.now(), "키워드 저장 성공");
    }

    @Override
    public BaseResponse<String> deleteKeyword(String userId, Long Keyword) {
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        KeywordInfoJpaEntity keywordEntity = keywordInfoRepository.findById(Keyword)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "키워드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        if (!keywordEntity.getUserKey().getUserKey().equals(userEntity.getUserKey())) {
            return new BaseResponse<>(false, "403", "키워드 삭제 권한 없음", OffsetDateTime.now(), "키워드 삭제 권한 없음");
        }
        log.info(keywordEntity.getKeyword());
        keywordInfoRepository.delete(keywordEntity);
        return new BaseResponse<>(true, "200", "키워드 삭제 성공", OffsetDateTime.now(), "키워드 삭제 성공");
    }

    @Override
    public BaseResponse<List<KeywordResponse>> getKeyword(String userId) {
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        List<KeywordResponse> keywordList = keywordInfoRepository.findByUserKey(userEntity).stream().map(keyword -> {
            KeywordResponse keywordRequest = new KeywordResponse();
            keywordRequest.setKeyword(keyword.getKeyword());
            keywordRequest.setKeywordId(keyword.getId());
            return keywordRequest;
        }).toList();
        return new BaseResponse<>(true, "200", "키워드 조회 성공", OffsetDateTime.now(), keywordList);
    }

    @Override
    public BaseResponse<String> pushKeywordAlarm(Long noticeId) throws IOException {    // 키워드 알림 전송
        log.info("pushKeywordAlarm: notice: {}", noticeId);
        NoticeJpaEntity noticeEntity = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "공지사항을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        List<KeywordInfoJpaEntity> noticeList = keywordInfoRepository.findKeywordsWithinTitle(noticeEntity.getTitle());
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            for (KeywordInfoJpaEntity keyword : noticeList) {
                Long userKey = keyword.getUserKey().getUserKey();
                UserInfoJpaEntity userEntity = userInfoRepository.findByUserKey(userKey)
                        .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다. userKey: " + userKey, HttpStatus.NOT_FOUND));
                NotificationHistoryJpaEntity notificationHistory = new NotificationHistoryJpaEntity();
                notificationHistory.setUserKey(userEntity);
                notificationHistory.setNotice(noticeEntity);
                notificationHistory.setCreatedAt(LocalDateTime.now());
                notificationHistoryRepository.save(notificationHistory);

                PushRequest.SendPushRequest sendPushRequest = PushRequest.SendPushRequest.builder()
                        .userKey(userKey)
                        .title("키워드 알림")
                        .content(noticeEntity.getTitle())
                        .pushType(PushType.NOTICE)
                        .moveToId("NOTICE" + noticeId)
                        .build();

                try {
                    sendRetry(sendPushRequest, userEntity.getAccessToken(), userEntity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return new BaseResponse<>( true, "200", "키워드 알림 전송 성공", OffsetDateTime.now(), "키워드 알림 전송 성공");
    }

    @Override
    public boolean pushTaxiJoinAlarm(Long postId) throws IOException {

        TaxiShareJpaEntity shareEntity = taxiShareRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Long userKey = shareEntity.getUserKey().getUserKey();
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserKey(userKey)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다. userKey: " + userKey, HttpStatus.NOT_FOUND));

        PushRequest.SendPushRequest sendPushRequest = PushRequest.SendPushRequest.builder()
                .userKey(userKey)
                .title("택시 파티 성공")
                .content(userEntity.getUserId() + "님이 올린 게시글에 모두 참여했습니다.")
                .pushType(PushType.TAXI)
                .moveToId("TAXI" + postId)
                .build();
        sendRetry(sendPushRequest, userEntity.getAccessToken(), userEntity);

        List<TaxiJoinJpaEntity> joins = taxiJoinRepository.findByPostId(shareEntity);
        for (TaxiJoinJpaEntity join : joins) {
            Long joinUserKey = join.getUserKey().getUserKey();
            UserInfoJpaEntity joinUserEntity = userInfoRepository.findByUserKey(joinUserKey)
                    .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다. userKey: " + joinUserKey, HttpStatus.NOT_FOUND));
            PushRequest.SendPushRequest sendJoinPushRequest = PushRequest.SendPushRequest.builder()
                    .userKey(userKey)
                    .title("택시 파티 성공")
                    .content(joinUserEntity.getUserId() + "님이 참가한 게시글에 모두 참여했습니다.")
                    .pushType(PushType.TAXI)
                    .moveToId("TAXI" + postId)
                    .build();
            CompletableFuture.runAsync(() -> {
                try {
                    sendRetry(sendJoinPushRequest, joinUserEntity.getAccessToken(), joinUserEntity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return true;
    }





    @Override
    public boolean pushTaxiCommentAlarm(Long postId, String content) throws IOException {   //택시 게시글 댓글 알림 전송
        TaxiShareJpaEntity shareEntity = taxiShareRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        Long userKey = shareEntity.getUserKey().getUserKey();
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserKey(userKey)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다. userKey: " + userKey, HttpStatus.NOT_FOUND));
        PushRequest.SendPushRequest sendPushRequest = PushRequest.SendPushRequest.builder()
                .userKey(userKey)
                .title("택시 게시글에 댓글이 달렸습니다.")
                .content(content)
                .pushType(PushType.COMMENT)
                .moveToId("TAXI" + postId)
                .build();
        sendRetry(sendPushRequest, userEntity.getAccessToken(), userEntity);

        return true;
    }

    private void sendRetry(PushRequest.SendPushRequest sendPushRequest, String token, UserInfoJpaEntity userEntity) throws IOException {
        boolean success = false;
        int retryCount = 0;
        int maxRetries = 3; // 최대 재시도 횟수

        while (!success && retryCount < maxRetries) {
            try {
                firebaseUtil.sendFcmTo(sendPushRequest, token);
                success = true; // 전송 성공
            } catch (IOException e) {
                retryCount++;
                log.error("FCM 전송 실패 (userKey: {}), 재시도 중... (시도 횟수: {})", userEntity.getUserKey(), retryCount);
                if (retryCount >= maxRetries) {
                    log.error("FCM 전송 실패 (userKey: {}) - 최대 재시도 횟수 초과", userEntity.getUserKey());
                }
                try {
                    Thread.sleep(1000); // 1초 대기
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 스레드 인터럽트 처리
                }
            }
        }
    }
}
