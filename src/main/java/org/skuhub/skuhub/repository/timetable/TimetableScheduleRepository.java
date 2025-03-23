package org.skuhub.skuhub.repository.timetable;

import org.skuhub.skuhub.model.timetable.TimetableScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableScheduleRepository extends JpaRepository<TimetableScheduleEntity, Integer> {

    /**
     * 조건 검색 쿼리
     */
    @Query("SELECT t FROM TimetableScheduleEntity t WHERE " +
            "(:year IS NULL OR t.year = :year) AND " +
            "(:semester IS NULL OR t.semester = :semester) AND " +
            "(:department IS NULL OR t.department = :department) AND " +
            "(:grade IS NULL OR t.grade = :grade) AND " +
            "(:subjectCode IS NULL OR t.subjectCode = :subjectCode) AND " +
            "(:subjectName IS NULL OR t.subjectName LIKE %:subjectName%) AND " +
            "(:professorName IS NULL OR t.professorName LIKE %:professorName%) AND " +
            "(:classTime IS NULL OR t.classTime = :classTime) AND " +
            "(:classroom IS NULL OR t.classroom = :classroom)")
    List<TimetableScheduleEntity> findSchedulesByConditions(
            @Param("year") Integer year,
            @Param("semester") Integer semester,
            @Param("department") String department,
            @Param("grade") Integer grade,
            @Param("subjectCode") Integer subjectCode,
            @Param("subjectName") String subjectName,
            @Param("professorName") String professorName,
            @Param("classTime") String classTime,
            @Param("classroom") String classroom
    );
}
