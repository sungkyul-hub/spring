package org.skuhub.skuhub.api.taxi.dto.request;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class TaxiCommentEditRequest {
    private Long postId;
    private Long commentId;
    private String commentContent;
}
