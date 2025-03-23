package org.skuhub.skuhub.api.taxi.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.skuhub.skuhub.api.taxi.dto.request.TaxiCommentRequest;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TaxiPostDetailsResponse {
    private Long postId;
    private String name;
    private String title;
    private String departureLocation;
    private String description;
    private OffsetDateTime rideTime;
    private int headCount; // 현재 인원
    private int numberOfPeople; // 총 인원
    private OffsetDateTime createdAt;
    private List<TaxiCommentResponse> comments;

    public TaxiPostDetailsResponse() {

    }
}
