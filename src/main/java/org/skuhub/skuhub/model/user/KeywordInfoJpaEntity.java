package org.skuhub.skuhub.model.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.auth.oauth2.TokenRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.skuhub.skuhub.model.taxi.TaxiCommentJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiJoinJpaEntity;
import org.skuhub.skuhub.model.taxi.TaxiShareJpaEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Entity
@Table(name = "KEYWORD_INFO_TB")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordInfoJpaEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private UserInfoJpaEntity userKey;

    @Size(max = 20)
    @Column(name = "keyword", length = 20)
    private String keyword;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Id
    @Column(name = "keyword_id", nullable = false)
    private Long id;

}
