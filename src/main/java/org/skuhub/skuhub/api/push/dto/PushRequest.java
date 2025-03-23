package org.skuhub.skuhub.api.push.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.skuhub.skuhub.common.enums.alarm.PushType;

@AllArgsConstructor
@Data
@ToString
public class PushRequest {
    @Setter
    @Getter
    @Builder
    public static class SendPushRequest {
        @Schema(description = "Push 를 보낼 유저 ID", example = "XXXXXXX")
        private Long userKey;
        @Schema(description = "Push 의 상단 제목", example = "title")
        private String title;
        @Schema(description = "Push 의 내용", example = "content")
        private String content;
        @Enumerated(EnumType.STRING)
        @Schema(description = "푸시 종류", example = "TAXI")
        private PushType pushType;
        @Schema(description = "클릭 후 이동해야될 고유 ID", example = "TAXI-XXXXXX")
        private String moveToId;
    }

    @Getter
    @Setter
    public static class SendChatPushRequest {
        private String userId;
        private String senderId;
        private String channelId;
        private String lastContent;
    }
}
