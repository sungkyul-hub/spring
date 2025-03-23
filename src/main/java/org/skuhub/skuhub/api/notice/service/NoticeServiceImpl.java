package org.skuhub.skuhub.api.notice.service;
import lombok.extern.slf4j.Slf4j;
import org.skuhub.skuhub.api.notice.dto.response.NoticeDetailsResponse;
import org.skuhub.skuhub.api.notice.dto.response.NoticeHistoryResponse;
import org.skuhub.skuhub.api.notice.dto.response.NoticeResponse;
import org.skuhub.skuhub.common.enums.exception.ErrorCode;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.skuhub.skuhub.exceptions.CustomException;
import org.skuhub.skuhub.model.notice.NoticeJpaEntity;
import org.skuhub.skuhub.model.notice.NotificationHistoryJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.skuhub.skuhub.repository.notice.NoticeRepository;
import org.skuhub.skuhub.repository.notice.NotificationHistoryRepository;
import org.skuhub.skuhub.repository.users.UserInfoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;
    private final UserInfoRepository userInfoRepository;

    public NoticeServiceImpl(NoticeRepository noticeRepository, NotificationHistoryRepository notificationHistoryRepository, UserInfoRepository userInfoRepository) {
        this.noticeRepository = noticeRepository;
        this.notificationHistoryRepository = notificationHistoryRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public BaseResponse<List<NoticeResponse>> searchNotice(String keyword, Long cursor, int limit) {
        List<NoticeJpaEntity> lastNoticeId = noticeRepository.findLastNoticeId();
        long cursorValue = cursor != null ? cursor : lastNoticeId.getFirst().getId() + 1;
        Pageable pageable = PageRequest.of(0, limit); // 첫 페이지 요청
        List<NoticeJpaEntity> noticeList = noticeRepository.findByTitleWithCursor(keyword, cursorValue, pageable);
        List<NoticeResponse> cursorList = noticeList.stream().map(notice -> {

            NoticeResponse response = new NoticeResponse();

            response.setNoticeId(notice.getId());
            response.setCategory(notice.getNoticeCategory());
            response.setTitle(notice.getTitle());
            response.setNoticeModifyDate(notice.getNoticeModifyDate());
            response.setWriter(notice.getWriter());

            // content를 30글자로 제한
            String content = notice.getNoticeContent();
            response.setContent(content.length() > 50 ? content.substring(0, 50) : content);

            return response;
        }).collect(Collectors.toList());


        if(cursorList.isEmpty()) {
            throw new CustomException(ErrorCode.NotFound, "검색 결과가 없습니다.", HttpStatus.NOT_FOUND);
        }

        return new BaseResponse<>(true, "200", "공지사항 검색 성공", OffsetDateTime.now(), cursorList);
    }

    @Override
    public BaseResponse<List<NoticeResponse>> categoryNotice(String category, Long cursor, int limit) {
        List<NoticeJpaEntity> lastNoticeId = noticeRepository.findLastNoticeId();
        long cursorValue = cursor != null ? cursor : lastNoticeId.getFirst().getId() + 1;
        Pageable pageable = PageRequest.of(0, limit); // 첫 페이지 요청
        List<NoticeResponse> noticeList = noticeRepository.findByCategory(category, cursorValue, pageable).stream().map(notice -> {

            NoticeResponse response = new NoticeResponse();

            response.setNoticeId(notice.getId());
            response.setCategory(notice.getNoticeCategory());
            response.setTitle(notice.getTitle());
            response.setNoticeModifyDate(notice.getNoticeModifyDate());
            response.setWriter(notice.getWriter());

            // content를 30글자로 제한
            String content = notice.getNoticeContent();
            response.setContent(content.length() > 50 ? content.substring(0, 50) : content);

            return response;
        }).collect(Collectors.toList());


        if(noticeList.isEmpty()) {
            throw new CustomException(ErrorCode.NotFound, "카테고리 결과가 없습니다.", HttpStatus.NOT_FOUND);
        }

        return new BaseResponse<>(true, "200", "공지사항 카테고리 검색 성공", OffsetDateTime.now(), noticeList);
    }

    @Override
    public BaseResponse<NoticeDetailsResponse> detailNotice(Long noticeId) {
        NoticeDetailsResponse noticeDetailsResponse = noticeRepository.findById(noticeId).map(notice -> {
            NoticeDetailsResponse response = new NoticeDetailsResponse();

            response.setNoticeId(notice.getId());
            response.setCategory(notice.getNoticeCategory());
            response.setTitle(notice.getTitle());
            response.setNoticeModifyDate(notice.getNoticeModifyDate());
            response.setWriter(notice.getWriter());
            response.setContent(notice.getNoticeContent());
            response.setNoticeOriginalContent(notice.getNoticeOriginalContent());
            response.setUrl(notice.getUrl());

            return response;
        }).orElseThrow(() -> new CustomException(ErrorCode.NotFound, "공지사항을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        return new BaseResponse<>(true, "200", "공지사항 상세보기 성공", OffsetDateTime.now(), noticeDetailsResponse);
    }

    @Override
    public BaseResponse<List<NoticeResponse>> noticeHistory(String userId) {
        UserInfoJpaEntity userEntity = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NotFound, "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        List<NoticeResponse> notificationHistory = notificationHistoryRepository.findByUserKey(userEntity).stream().map(history -> {
            NoticeResponse response = new NoticeResponse();

            response.setNoticeId(history.getNotice().getId());
            response.setCategory(history.getNotice().getNoticeCategory());
            response.setTitle(history.getNotice().getTitle());
            response.setNoticeModifyDate(history.getNotice().getNoticeModifyDate());
            response.setWriter(history.getNotice().getWriter());

            // content를 30글자로 제한
            String content = history.getNotice().getNoticeContent();
            response.setContent(content.length() > 50 ? content.substring(0, 50) : content);

            return response;
        }).toList();
        return new BaseResponse<>(true, "200", "공지사항 히스토리 검색 성공", OffsetDateTime.now(), notificationHistory);
    }

    public void saveNotice(String category, String title, LocalDate date, LocalDate modifyDate,
                           String writer, String originalContent, String content, String url) {

        // 중복 체크 (예시: 같은 제목 존재 시 저장 안함)
        if (noticeRepository.existsByTitle(title)) {
            return;
        }

        NoticeJpaEntity notice = NoticeJpaEntity.builder()
                .noticeCategory(category)
                .title(title)
                .noticeDate(date)
                .noticeModifyDate(modifyDate)
                .writer(writer)
                .noticeOriginalContent(originalContent)
                .noticeContent(content)
                .url(url)
                .createdAt(Instant.from(LocalDateTime.now()))
                .updatedAt(Instant.from(LocalDateTime.now()))
                .build();

        noticeRepository.save(notice);
    }

}
