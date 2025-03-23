package org.skuhub.skuhub.api.notice.service;
import org.skuhub.skuhub.api.notice.dto.response.NoticeDetailsResponse;
import org.skuhub.skuhub.api.notice.dto.response.NoticeResponse;
import org.skuhub.skuhub.common.response.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface NoticeService {
    public BaseResponse<List<NoticeResponse>> searchNotice(String keyword, Long cursor, int limit);

    public BaseResponse<List<NoticeResponse>> categoryNotice(String category, Long cursor, int limit);

    public BaseResponse<NoticeDetailsResponse> detailNotice(Long noticeId);

    BaseResponse<List<NoticeResponse>> noticeHistory(String userId);
}
