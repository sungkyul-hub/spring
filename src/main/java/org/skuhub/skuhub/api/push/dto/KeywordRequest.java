package org.skuhub.skuhub.api.push.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class KeywordRequest {
    private String keyword;
}
