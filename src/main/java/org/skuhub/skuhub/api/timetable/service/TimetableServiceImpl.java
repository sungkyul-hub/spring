package org.skuhub.skuhub.api.timetable.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.skuhub.skuhub.api.timetable.dto.request.UserTimetableRequest;
import org.skuhub.skuhub.api.timetable.dto.request.UpdateScoreRequest;
import org.skuhub.skuhub.api.timetable.dto.request.UpdateUserTimetableRequest;
import org.skuhub.skuhub.api.timetable.dto.request.CompletionRequest;
import org.skuhub.skuhub.api.timetable.dto.response.UserTimetableResponse;
import org.skuhub.skuhub.model.timetable.TimetableScheduleEntity;
import org.skuhub.skuhub.model.timetable.UserTimetableEntity;
import org.skuhub.skuhub.model.timetable.CompletionEntity;
import org.skuhub.skuhub.model.timetable.PersonalTimetableEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.skuhub.skuhub.repository.timetable.TimetableScheduleRepository;
import org.skuhub.skuhub.repository.timetable.UserTimetableRepository;
import org.skuhub.skuhub.repository.timetable.CompletionRepository;
import org.skuhub.skuhub.repository.users.UserInfoRepository;
import org.skuhub.skuhub.repository.timetable.PersonalTimetableRepository;
import org.springframework.stereotype.Service;

@Service
public class TimetableServiceImpl implements TimetableService {

    private final TimetableScheduleRepository timetableScheduleRepository;
    private final UserTimetableRepository userTimetableRepository;
    private final CompletionRepository completionRepository;
    private final UserInfoRepository userInfoRepository;
    private final PersonalTimetableRepository personalTimetableRepository;

    public TimetableServiceImpl(TimetableScheduleRepository timetableScheduleRepository,
                                UserTimetableRepository userTimetableRepository,
                                CompletionRepository completionRepository,
                                UserInfoRepository userInfoRepository,
                                PersonalTimetableRepository personalTimetableRepository) {
        this.timetableScheduleRepository = timetableScheduleRepository;
        this.userTimetableRepository = userTimetableRepository;
        this.completionRepository = completionRepository;
        this.userInfoRepository = userInfoRepository;
        this.personalTimetableRepository = personalTimetableRepository;

    }

    @Override
    public List<TimetableScheduleEntity> getAllSchedules() {
        return timetableScheduleRepository.findAll();
    }

    @Override
    public void createUserTimetable(UserTimetableRequest request) {
        UserTimetableEntity timetable = UserTimetableEntity.builder()
                .userKey(request.getUserKey())
                .timetableId(request.getTimetableId())
                .personalGrade(request.getPersonalGrade())
                .personalSemester(request.getPersonalSemester())
                .score(request.getScore())
                .majorStatus(request.getMajorStatus())
                .build();
        userTimetableRepository.save(timetable);
    }

    @Override
    public List<UserTimetableResponse> getUserTimetable(Integer userKey) {
        return userTimetableRepository.findByUserKey(userKey)
                .stream()
                .map(UserTimetableResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public boolean updateUserTimetable(UpdateUserTimetableRequest request) {
        if (request.getPersonalKey() == null) {
            throw new IllegalArgumentException("Error: Personal Key is missing! Please provide a valid personal_key.");
        }
        Optional<UserTimetableEntity> timetableOptional = userTimetableRepository.findById(request.getPersonalKey());
        if (timetableOptional.isEmpty()) {
            throw new IllegalArgumentException("Error: Timetable not found for given personal_key: " + request.getPersonalKey());
        }
        UserTimetableEntity timetable = timetableOptional.get();

        if (request.getUserKey() != null) {
            timetable.setUserKey(request.getUserKey());
        }
        if (request.getTimetableId() != null) {
            timetable.setTimetableId(request.getTimetableId());
        }
        if (request.getPersonalGrade() != null) {
            timetable.setPersonalGrade(request.getPersonalGrade());
        }
        if (request.getPersonalSemester() != null) {
            timetable.setPersonalSemester(request.getPersonalSemester());
        }
        if (request.getScore() != null) {
            timetable.setScore(request.getScore());
        }
        if (request.getMajorStatus() != null) {
            timetable.setMajorStatus(request.getMajorStatus());
        }
        userTimetableRepository.save(timetable);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteUserTimetable(Integer personalKey) {
        Optional<UserTimetableEntity> timetableOptional = userTimetableRepository.findById(personalKey);
        if (timetableOptional.isEmpty()) {
            return false;
        }
        userTimetableRepository.delete(timetableOptional.get());
        return true;
    }

    @Override
    @Transactional
    public boolean addScoreToUserTimetable(Integer personalKey, String score, Integer majorStatus) {
        Optional<UserTimetableEntity> timetableOptional = userTimetableRepository.findById(personalKey);
        if (timetableOptional.isEmpty()) {
            return false;
        }
        UserTimetableEntity timetable = timetableOptional.get();
        if (score != null) {
            timetable.setScore(score);
        }
        if (majorStatus != null) {
            timetable.setMajorStatus(majorStatus);
        }
        userTimetableRepository.save(timetable);
        return true;
    }

    @Override
    @Transactional
    public boolean updateUserScore(UpdateScoreRequest request) {
        Optional<UserTimetableEntity> timetableOptional = userTimetableRepository.findById(request.getPersonalKey());
        if (timetableOptional.isEmpty()) {
            return false;
        }
        UserTimetableEntity timetable = timetableOptional.get();
        timetable.setScore(request.getScore());
        if (request.getMajorStatus() != null) {
            timetable.setMajorStatus(request.getMajorStatus());
        }
        userTimetableRepository.save(timetable);
        return true;
    }

    @Override
    @Transactional
    public CompletionEntity saveCompletionData(CompletionRequest request) {
        // userKey 타입을 Long 으로 변환
        Long userKey = request.getUserKey().longValue();

        Optional<UserInfoJpaEntity> userOptional = userInfoRepository.findById(userKey);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 user_key입니다.");
        }
        UserInfoJpaEntity user = userOptional.get();

        // 이미 존재하는 completion 데이터 확인
        Optional<CompletionEntity> existingCompletion = completionRepository.findByUserInfoTb_UserKey(userKey);

        CompletionEntity completion;
        if (existingCompletion.isPresent()) {
            completion = existingCompletion.get();
        } else {
            completion = new CompletionEntity();
            completion.setUserInfoTb(user);
        }

        // 데이터 설정
        completion.setMajorRequired(request.getMajorRequired());
        completion.setMajorElective(request.getMajorElective());
        completion.setGeneralRequired(request.getGeneralRequired());
        completion.setGeneralElective(request.getGeneralElective());
        completion.setMajorRequiredReq(request.getMajorRequiredReq());
        completion.setMajorElectiveReq(request.getMajorElectiveReq());
        completion.setGeneralRequiredReq(request.getGeneralRequiredReq());
        completion.setGeneralElectiveReq(request.getGeneralElectiveReq());
        completion.setOther(request.getOther());
        completion.setEarnedCredits(request.getEarnedCredits());
        completion.setGraduationCredits(request.getGraduationCredits());
        completion.setAvgScore(request.getAvgScore());
        completion.setUpdatedAt(java.time.OffsetDateTime.now());

        return completionRepository.save(completion);
    }

    @Override
    @Transactional
    public CompletionEntity updateCompletionData(CompletionRequest request) {
        Optional<CompletionEntity> existingCompletion = completionRepository.findByUserInfoTb_UserKey(request.getUserKey().longValue());
        if (existingCompletion.isEmpty()) {
            throw new IllegalArgumentException("해당 user_key에 대한 이수구분 정보를 찾을 수 없습니다.");
        }
        CompletionEntity completion = existingCompletion.get();
        if (request.getMajorRequired() != null) {
            completion.setMajorRequired(request.getMajorRequired());
        }
        if (request.getMajorElective() != null) {
            completion.setMajorElective(request.getMajorElective());
        }
        if (request.getGeneralRequired() != null) {
            completion.setGeneralRequired(request.getGeneralRequired());
        }
        if (request.getGeneralElective() != null) {
            completion.setGeneralElective(request.getGeneralElective());
        }
        if (request.getMajorRequiredReq() != null) {
            completion.setMajorRequiredReq(request.getMajorRequiredReq());
        }
        if (request.getMajorElectiveReq() != null) {
            completion.setMajorElectiveReq(request.getMajorElectiveReq());
        }
        if (request.getGeneralRequiredReq() != null) {
            completion.setGeneralRequiredReq(request.getGeneralRequiredReq());
        }
        if (request.getGeneralElectiveReq() != null) {
            completion.setGeneralElectiveReq(request.getGeneralElectiveReq());
        }
        if (request.getOther() != null) {
            completion.setOther(request.getOther());
        }
        if (request.getEarnedCredits() != null) {
            completion.setEarnedCredits(request.getEarnedCredits());
        }
        if (request.getGraduationCredits() != null) {
            completion.setGraduationCredits(request.getGraduationCredits());
        }
        if (request.getAvgScore() != null) {
            completion.setAvgScore(request.getAvgScore());
        }
        completion.setUpdatedAt(java.time.OffsetDateTime.now());
        return completionRepository.save(completion);
    }

    @Override
    public List<PersonalTimetableEntity> getUserPersonalTimetable(Integer userKey) {
        return personalTimetableRepository.findByUserKey(userKey);
    }

    @Override
    public List<TimetableScheduleEntity> findSchedulesByConditions(
            Integer year,
            Integer semester,
            String department,
            Integer grade,
            Integer subjectCode,
            String subjectName,
            String professorName,
            String classTime,
            String classroom) {

        return timetableScheduleRepository.findAll().stream().filter(entity -> {
            boolean matches = true;
            if (year != null) {
                matches = matches && year.equals(entity.getYear());
            }
            if (semester != null) {
                matches = matches && semester.equals(entity.getSemester());
            }
            if (department != null) {
                matches = matches && department.equals(entity.getDepartment());
            }
            if (grade != null) {
                matches = matches && grade.equals(entity.getGrade());
            }
            if (subjectCode != null) {
                matches = matches && subjectCode.equals(entity.getSubjectCode());
            }
            if (subjectName != null) {
                matches = matches && subjectName.equals(entity.getSubjectName());
            }
            if (professorName != null) {
                matches = matches && professorName.equals(entity.getProfessorName());
            }
            if (classTime != null) {
                matches = matches && classTime.equals(entity.getClassTime());
            }
            if (classroom != null) {
                matches = matches && classroom.equals(entity.getClassroom());
            }
            return matches;
        }).toList();
    }
    @Override
    public Integer findUserKeyByUserId(String userId) {
        Optional<UserInfoJpaEntity> userOptional = userInfoRepository.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("해당 userId를 가진 사용자를 찾을 수 없습니다: " + userId);
        }
        return userOptional.get().getUserKey().intValue();
    }
}