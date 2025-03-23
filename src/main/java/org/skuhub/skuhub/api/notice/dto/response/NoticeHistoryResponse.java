package org.skuhub.skuhub.api.notice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class NoticeHistoryResponse {
    List<NoticeResponse> notices;
}
