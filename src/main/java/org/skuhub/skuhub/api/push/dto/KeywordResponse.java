package org.skuhub.skuhub.api.push.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class KeywordResponse {
    Long keywordId;
    String keyword;

    public KeywordResponse() {

    }
}
