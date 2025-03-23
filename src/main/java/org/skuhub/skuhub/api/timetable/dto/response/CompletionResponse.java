package org.skuhub.skuhub.api.timetable.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompletionResponse {
    private Long userKey;
    private Instant updatedAt;
}