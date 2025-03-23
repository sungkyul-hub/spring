package org.skuhub.skuhub.model.timetable;

import jakarta.persistence.*;
import lombok.*;
import org.skuhub.skuhub.model.BaseTime;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "CREDIT_CATEGORY_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompletionEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_key", referencedColumnName = "user_key", nullable = false)
    private UserInfoJpaEntity userInfoTb;

    @Column(name = "major_required")
    private Integer majorRequired;

    @Column(name = "major_elective")
    private Integer majorElective;

    @Column(name = "general_required")
    private Integer generalRequired;

    @Column(name = "general_elective")
    private Integer generalElective;

    @Column(name = "major_required_req")
    private Integer majorRequiredReq;

    @Column(name = "major_elective_req")
    private Integer majorElectiveReq;

    @Column(name = "general_required_req")
    private Integer generalRequiredReq;

    @Column(name = "general_elective_req")
    private Integer generalElectiveReq;

    @Column(name = "other")
    private Integer other;

    @Column(name = "earned_credits")
    private Integer earnedCredits;

    @Column(name = "avg_score", precision = 4, scale = 2)
    private BigDecimal avgScore;

    @Column(name = "graduation_credits")
    private Integer graduationCredits;
}