package org.skuhub.skuhub.model.notice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "NOTICE_TB")
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @Column(name = "notice_category", nullable = false, length = 20)
    private String noticeCategory;

    @Size(max = 500)
    @NotNull
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @NotNull
    @Column(name = "notice_date", nullable = false)
    private LocalDate noticeDate;

    @NotNull
    @Column(name = "notice_modify_date", nullable = false)
    private LocalDate noticeModifyDate;

    @Size(max = 30)
    @NotNull
    @Column(name = "writer", nullable = false, length = 30)
    private String writer;

    @NotNull
    @Lob
    @Column(name = "notice_original_content", nullable = false)
    private String noticeOriginalContent;

    @Size(max = 10000)
    @NotNull
    @Column(name = "notice_content", nullable = false, length = 10000)
    private String noticeContent;

    @Size(max = 500)
    @NotNull
    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "notice")
    private Set<NotificationHistoryJpaEntity> notificationHistories = new LinkedHashSet<>();

}
