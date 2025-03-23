package org.skuhub.skuhub.model.timetable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.skuhub.skuhub.model.BaseTime;

@Entity
@Table(name = "TIMETABLE_TB")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableScheduleEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Size(max = 10)
    @NotNull
    @Column(name = "department", nullable = false, length = 10)
    private String department;

    @NotNull
    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Size(max = 10)
    @NotNull
    @Column(name = "completion_type", nullable = false, length = 10)
    private String completionType;

    @NotNull
    @Column(name = "subject_code", nullable = false)
    private Integer subjectCode;

    @NotNull
    @Column(name = "class_number", nullable = false)
    private Integer classNumber;

    @Size(max = 20)
    @NotNull
    @Column(name = "subject_name", nullable = false, length = 20)
    private String subjectName;

    @NotNull
    @Column(name = "credit", nullable = false)
    private Integer credit;

    @Size(max = 50)
    @NotNull
    @Column(name = "professor_name", nullable = false, length = 50)
    private String professorName;

    @Size(max = 10)
    @NotNull
    @Column(name = "class_time", nullable = false, length = 10)
    private String classTime;

    @Size(max = 20)
    @NotNull
    @Column(name = "classroom", nullable = false, length = 20)
    private String classroom;

    @Size(max = 10)
    @Column(name = "general_area", length = 10)
    private String generalArea;
}
