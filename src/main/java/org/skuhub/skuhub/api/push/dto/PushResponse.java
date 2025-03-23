package org.skuhub.skuhub.api.push.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.skuhub.skuhub.common.enums.alarm.PushType;

import java.time.OffsetDateTime;
import java.util.List;
@AllArgsConstructor
@Data
@ToString
public class PushResponse {
    @Getter
    @Builder
    public static class GetPushResponse {
        private String title;
        private String content;
        private PushType pushType;
        private String moveToId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        private OffsetDateTime createdAt;
    }

    @Getter
    @Builder
    public static class GetPushListResponse {
        List<GetPushResponse> pushList;
    }
    @Getter
    @Builder
    public static class GetReviewAlertResponse{
        private String title;
        private String image;
        private String itemId;
        private String startDate;
        private String endDate;
    }
}
