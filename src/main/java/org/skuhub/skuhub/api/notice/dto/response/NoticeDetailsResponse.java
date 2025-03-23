package org.skuhub.skuhub.api.notice.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NoticeDetailsResponse {
    private Long noticeId;
    private String category;
    private String title;
    private LocalDate noticeModifyDate;
    private String writer;
    private String content;
    private String noticeOriginalContent;
    private String url;

    public NoticeDetailsResponse() {

    }
}
