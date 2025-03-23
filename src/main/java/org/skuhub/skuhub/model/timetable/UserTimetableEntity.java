package org.skuhub.skuhub.model.timetable;

import jakarta.persistence.*;
import lombok.*;
import org.skuhub.skuhub.model.BaseTime;

@Entity
@Table(name = "PERSONAL_TIMETABLE_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTimetableEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_key", nullable = false)
    private Integer personalKey;

    @Column(name = "user_key", nullable = false)
    private Integer userKey;

    @Column(name = "timetable_id", nullable = false)
    private Integer timetableId;

    @Column(name = "personal_grade", nullable = false)
    private String personalGrade;

    @Column(name = "personal_semester", nullable = false)
    private String personalSemester;

    // createdAt과 updatedAt은 BaseTime에서 관리

    @Column(name = "score")
    private String score;

    @Column(name = "major_status")
    private Integer majorStatus;
}
