package org.skuhub.skuhub.model.taxi;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;

@Entity
@Getter
@Setter
@Table(name = "COMMENT_TB")
public class TaxiCommentJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private TaxiShareJpaEntity postId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    private UserInfoJpaEntity userKey;

    @Size(max = 100)
    @NotNull
    @Column(name = "comment_content", nullable = false, length = 100)
    private String commentContent;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private java.time.OffsetDateTime createdAt;

}
