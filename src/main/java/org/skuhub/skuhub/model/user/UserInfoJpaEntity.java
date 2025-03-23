package org.skuhub.skuhub.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.auth.oauth2.TokenRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.skuhub.skuhub.model.notice.NotificationHistoryJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiCommentJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiJoinJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "USER_INFO_TB")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserInfoJpaEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_key")
        private Long userKey;  // user_key로 매핑

        @Column(name = "user_id", nullable = false, unique = true, length = 20)
        private String userId;   // 유니크한 값

        @Column(name = "email", nullable = false, unique = true, length = 100)
        private String email;

        @Column(name = "password", nullable = false, length = 510)
        private String password;

        @Column(name = "year", nullable = false, length = 4)
        private int year;

        @Column(name = "department", nullable = false, length = 20)
        private String department;

        @Column(name = "name", nullable = false, length = 20)
        private String name;

        @Setter
        @Size(max = 255)
        @Column(name = "Access_token")
        private String accessToken;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "withdrawal_date")
        private LocalDateTime withdrawalDate;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Setter
        @OneToMany(mappedBy = "userKey")
        private Set<TaxiCommentJpaEntity> commentTbs = new LinkedHashSet<>();

        @Setter
        @OneToMany(mappedBy = "userKey")
        private Set<TaxiJoinJpaEntity> taxiJoinTbs = new LinkedHashSet<>();

        @Setter
        @OneToMany(mappedBy = "userKey")
        private Set<TaxiShareJpaEntity> taxiShareTbs = new LinkedHashSet<>();



        @Setter
        @OneToMany(mappedBy = "userKey")
        private Set<NotificationHistoryJpaEntity> notificationHistories = new LinkedHashSet<>();

        @Setter
        @OneToOne(mappedBy = "userKey")
        private KeywordInfoJpaEntity keywordInfoTb;

}
