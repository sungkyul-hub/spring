package org.skuhub.skuhub.model.taxi;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "TAXI_SHARE_TB")
public class TaxiShareJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private UserInfoJpaEntity userKey;


    @Size(max = 20)
    @NotNull
    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Size(max = 20)
    @NotNull
    @Column(name = "departure_location", nullable = false, length = 20)
    private String departureLocation;

    @NotNull
    @Column(name = "ride_time", nullable = false)
    private java.time.OffsetDateTime rideTime;

    @NotNull
    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "head_count", nullable = false)
    private Integer headCount = 0;

    @Size(max = 200)
    @NotNull
    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


//    @PrePersist
//    protected void onCreate() {
//        LocalDateTime now = LocalDateTime.now();
//        this.createdAt = now;
//        this.updatedAt = now;
//    }

    @OneToMany(mappedBy = "postId")
    private Set<TaxiCommentJpaEntity> commentTbs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "postId")
    private Set<TaxiJoinJpaEntity> taxiJoinTbs = new LinkedHashSet<>();

}
