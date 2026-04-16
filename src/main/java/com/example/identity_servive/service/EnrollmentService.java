package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.EnrollmentRequest;
import com.example.identity_servive.dto.response.EnrollmentResponse;
import com.example.identity_servive.entity.*;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.EnrollmentMapper;
import com.example.identity_servive.mapper.UserMapper;
import com.example.identity_servive.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j // Hỗ trợ ghi lại lịch sử hoạt động (Logging)
@Service // Đăng ký lớp này là một Service do Spring quản lý (Bean)
@RequiredArgsConstructor // Tự động tạo Constructor để tiêm (Inject) các Repository và Mapper vào
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentService {
    EnrollmentRepository enrollmentRepository;
    LanguageRepository languageRepository;
    UserRepository userRepository;
    EnrollmentMapper enrollmentMapper;
    ChapterRepository chapterRepository;
    LessonRepository lessonRepository;
    UserChapterProgressRepository userChapterProgressRepository;
    UserLessonProgressRepository userLessonProgressRepository;
    UserStepProgressRepository userStepProgressRepository;
    StepRepository stepRepository;

    @Transactional
    public EnrollmentResponse enrollCourse(EnrollmentRequest request) {
        var context = SecurityContextHolder.getContext();

        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Language language = languageRepository.findById(request.languageName())
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByUserAndLanguage(user, language);
        Enrollment enrollment;
        if (enrollmentOpt.isPresent()) {
            enrollment = enrollmentOpt.get();
        } else {
            enrollment = Enrollment.builder()
                    .user(user)
                    .language(language)
                    .currentXp(0.0)
                    .progressPercentage(0.0)
                    .status(Status.IN_PROGRESS)
                    .build();
            enrollmentRepository.save(enrollment);
            unlockFirstContent(enrollment, user);
        }

//        var userLearning = enrollmentRepository.findAllByUser(user);
//        List<String> users =  userLearning.stream().map(enr -> enr.getUser().getUsername()).toList();
        EnrollmentResponse enrollmentResponse = enrollmentMapper.toEnrollmentResponse(enrollment);
        enrollmentResponse.setUserName(username);
        return enrollmentResponse;
    }

    @Transactional
    protected void unlockFirstContent(Enrollment enrollment, User user) {
        Language language = enrollment.getLanguage();

        Chapter firstChapter = chapterRepository.findFirstByLanguageOrderByOrderIndexAsc(language)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        UserChapterProgress userChapterProgress = UserChapterProgress.builder()
                .user(user)
                .chapter(firstChapter)
                .lockedStatus(IsLocked.FALSE_LOCKED)
                .completedStatus(IsCompleted.FALSE)
                .build();
        userChapterProgressRepository.save(userChapterProgress);

        Lesson firstLesson = lessonRepository.findFirstByChapterOrderByOrderIndexAsc(firstChapter)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

        UserLessonProgress userLessonProgress = UserLessonProgress
                .builder()
                .user(user)
                .lesson(firstLesson)
                .lockedStatus(IsLocked.FALSE_LOCKED)
                .completedStatus(IsCompleted.FALSE)
                .build();
        userLessonProgressRepository.save(userLessonProgress);

        Step firstStep = stepRepository.findFirstByLessonIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(firstLesson.getId(), 0)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
        UserStepProgress userStepProgress = UserStepProgress.builder()
                .user(user)
                .step(firstStep)
                .lockedStatus(IsLocked.FALSE_LOCKED)
                .completedStatus(IsCompleted.FALSE)
                .build();
        userStepProgressRepository.save(userStepProgress);


    }
}
