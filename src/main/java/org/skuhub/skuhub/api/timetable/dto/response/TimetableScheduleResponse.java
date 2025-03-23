package org.skuhub.skuhub.api.timetable.dto.response;

import lombok.*;
import java.time.Instant;
import org.skuhub.skuhub.model.timetable.TimetableScheduleEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableScheduleResponse {
    private Integer id;
    private Integer year;
    private Integer semester;
    private String department;
    private Integer grade;
    private String completionType;
    private Integer subjectCode;
    private Integer classNumber;
    private String subjectName;
    private Integer credit;
    private String professorName;
    private String classTime;
    private String classroom;
    private String generalArea;
    private Instant createdAt;

    public static TimetableScheduleResponse fromEntity(TimetableScheduleEntity entity) {
        return new TimetableScheduleResponse(
                entity.getId(),
                entity.getYear(),
                entity.getSemester(),
                entity.getDepartment(),
                entity.getGrade(),
                entity.getCompletionType(),
                entity.getSubjectCode(),
                entity.getClassNumber(),
                entity.getSubjectName(),
                entity.getCredit(),
                entity.getProfessorName(),
                entity.getClassTime(),
                entity.getClassroom(),
                entity.getGeneralArea(),
                entity.getCreatedAt().toInstant()
        );
    }
}

