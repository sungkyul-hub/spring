package org.skuhub.skuhub.model.notice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "NOTIFICATION_HISTORY")
public class NotificationHistoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notifi_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private NoticeJpaEntity notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private UserInfoJpaEntity userKey;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}