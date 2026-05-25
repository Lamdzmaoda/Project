/* (C)2026 */
package com.example.identity_servive.service.learning;

import com.example.identity_servive.dto.request.learningRequest.EnrollmentRequest;
import com.example.identity_servive.dto.response.progress.EnrollmentResponse;
import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.progress.Enrollment;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.mapper.EnrollmentMapper;
import com.example.identity_servive.repository.Progress.EnrollmentRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.learning.LanguageRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // Hỗ trợ ghi lại lịch sử hoạt động (Logging)
@Service // Đăng ký lớp này là một Service do Spring quản lý (Bean)
@RequiredArgsConstructor // Tự động tạo Constructor để tiêm (Inject) các Repository và Mapper vào
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentService {
  EnrollmentRepository enrollmentRepository;
  LanguageRepository languageRepository;
  UserRepository userRepository;
  EnrollmentMapper enrollmentMapper;
  LearningProgressService learningProgressService;

  @Transactional
  public EnrollmentResponse enrollCourse(EnrollmentRequest request) {
    var context = SecurityContextHolder.getContext();

    String username = Objects.requireNonNull(context.getAuthentication()).getName();
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    Language language =
        languageRepository
            .findById(request.languageName())
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));
    Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByUserAndLanguage(user, language);
    Enrollment enrollment;
    if (enrollmentOpt.isPresent()) {
      enrollment = enrollmentOpt.get();
    } else {
      enrollment =
          Enrollment.builder()
              .user(user)
              .language(language)
              .currentXp(0)
              .progressPercentage(0)
              .status(Status.IN_PROGRESS)
              .build();
      enrollmentRepository.save(enrollment);
      learningProgressService.initializeLearningProgressForLanguage(language, user);
    }

    EnrollmentResponse enrollmentResponse = enrollmentMapper.toEnrollmentResponse(enrollment);
    enrollmentResponse.setUserName(username);
    return enrollmentResponse;
  }
}
