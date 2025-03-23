package org.skuhub.skuhub.api.taxi.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;

@Getter
@Setter
public class TaxiCommentRequest {

    private Long postId;
    private String name;
    private String commentContent;
}
