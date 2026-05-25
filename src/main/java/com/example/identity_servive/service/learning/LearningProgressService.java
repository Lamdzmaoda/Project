/* (C)2026 */
package com.example.identity_servive.service.learning;

import com.example.identity_servive.entity.auth.User;
import com.example.identity_servive.entity.learning.Chapter;
import com.example.identity_servive.entity.learning.Language;
import com.example.identity_servive.entity.learning.Lesson;
import com.example.identity_servive.entity.progress.Enrollment;
import com.example.identity_servive.entity.progress.UserChapterProgress;
import com.example.identity_servive.entity.progress.UserLessonProgress;
import com.example.identity_servive.enums.ContentStatus;
import com.example.identity_servive.enums.IsCompleted;
import com.example.identity_servive.enums.IsLocked;
import com.example.identity_servive.exception.AppException;
import com.example.identity_servive.exception.ErrorCode;
import com.example.identity_servive.repository.Progress.EnrollmentRepository;
import com.example.identity_servive.repository.Progress.SubmissionRepository;
import com.example.identity_servive.repository.Progress.UserChapterProgressRepository;
import com.example.identity_servive.repository.Progress.UserLessonProgressRepository;
import com.example.identity_servive.repository.auth.UserRepository;
import com.example.identity_servive.repository.learning.ChapterRepository;
import com.example.identity_servive.repository.learning.LanguageRepository;
import com.example.identity_servive.repository.learning.LessonRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j // Cho phép dùng lệnh log.info() để ghi nhật ký hoạt động
@Service // Đánh dấu lớp này là một Service để Spring quản lý
@RequiredArgsConstructor // Tự động tạo constructor cho các biến 'final' (Dependency Injection)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Mặc định các biến là private final
public class LearningProgressService {
  ChapterRepository chapterRepository;
  UserChapterProgressRepository userChapterProgressRepository;
  LessonRepository lessonRepository;
  UserLessonProgressRepository userLessonProgressRepository;
  UserRepository userRepository;
  EnrollmentRepository enrollmentRepository;
  LanguageRepository languageRepository;
  SubmissionRepository submissionRepository;

  @Transactional
  public void unlockNextChapter(User user, Chapter nextChapter) {

    unlockChapterProgress(user, nextChapter);

    // Mở TẤT CẢ lesson trong chapter mới
    lessonRepository
        .findAllByChapterIdAndStatusOrderByOrderIndexAsc(nextChapter.getId(), ContentStatus.ACTIVE)
        .forEach(lesson -> unlockLessonProgress(user, lesson));
  }

  @Transactional
  public void updateLessonToCompleted(User user, Lesson lesson) {
    // Tìm bản ghi tiến trình của user cho lesson này
    // Nếu chưa có → tạo mới (builder)
    UserLessonProgress progress =
        userLessonProgressRepository
            .findByUserAndLesson(user, lesson)
            .orElseGet(() -> UserLessonProgress.builder().user(user).lesson(lesson).build());
    if (progress != null && IsCompleted.TRUE.equals(progress.getCompletedStatus())) {
      return;
    }
    // Mở khóa (để user có thể xem lại bài đã học)
    assert progress != null;
    progress.setLockedStatus(IsLocked.FALSE_LOCKED);
    // Đánh dấu hoàn thành
    progress.setCompletedStatus(IsCompleted.TRUE);
    // Lưu vào DB
    userLessonProgressRepository.save(progress);
  }

  @Transactional
  public void updateChapterToCompleted(User user, Chapter chapter) {
    // Tương tự updateLessonToCompleted nhưng cho chapter
    UserChapterProgress progress =
        userChapterProgressRepository
            .findByUserAndChapter(user, chapter)
            .orElseGet(() -> UserChapterProgress.builder().user(user).chapter(chapter).build());
    if (progress != null && IsCompleted.TRUE.equals(progress.getCompletedStatus())) {
      return;
    }
    assert progress != null;
    progress.setLockedStatus(IsLocked.FALSE_LOCKED);
    progress.setCompletedStatus(IsCompleted.TRUE);
    userChapterProgressRepository.save(progress);
  }

  @Transactional
  public void unlockLessonProgress(User user, Lesson lesson) {
    // Tìm hoặc tạo mới bản ghi tiến trình
    UserLessonProgress progress =
        userLessonProgressRepository
            .findByUserAndLesson(user, lesson)
            .orElseGet(
                () ->
                    UserLessonProgress.builder()
                        .user(user)
                        .lesson(lesson)
                        .completedStatus(IsCompleted.FALSE) // Chưa hoàn thành
                        .build());

    // Mở khóa bài học
    progress.setLockedStatus(IsLocked.FALSE_LOCKED);
    // Đặt trạng thái chưa hoàn thành
    if (!IsCompleted.TRUE.equals(progress.getCompletedStatus())) {
      progress.setCompletedStatus(IsCompleted.FALSE);
    }
    // Lưu vào DB
    userLessonProgressRepository.save(progress);
  }

  @Transactional
  public void unlockChapterProgress(User user, Chapter chapter) {
    // Tương tự unlockLessonProgress nhưng cho chapter
    UserChapterProgress progress =
        userChapterProgressRepository
            .findByUserAndChapter(user, chapter)
            .orElseGet(
                () ->
                    UserChapterProgress.builder()
                        .user(user)
                        .chapter(chapter)
                        .completedStatus(IsCompleted.FALSE)
                        .build());

    progress.setLockedStatus(IsLocked.FALSE_LOCKED);
    progress.setCompletedStatus(IsCompleted.FALSE);
    userChapterProgressRepository.save(progress);
  }

  public User getCurrentUser() {
    // Lấy thông tin authentication từ Security Context của Spring
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // Nếu không có authentication → chưa đăng nhập → lỗi
    if (authentication == null || authentication.getName() == null) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    // Tìm user trong DB bằng username (email/SĐT đăng nhập)
    return userRepository
        .findByUsername(authentication.getName())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
  }

  @Transactional
  public void initializeLearningProgressForLanguage(Language language, User user) {
    // Tìm chapter đầu tiên của language (theo orderIndex tăng dần)
    Chapter firstChapter =
        chapterRepository
            .findFirstByLanguageAndStatusOrderByOrderIndexAsc(language, ContentStatus.ACTIVE)
            .orElseThrow(() -> new AppException(ErrorCode.COURSE_UNDER_CONSTRUCTION));

    // Tạo hoặc lấy tiến trình chapter của user
    UserChapterProgress userChapterProgress =
        userChapterProgressRepository
            .findByUserAndChapter(user, firstChapter)
            .orElseGet(
                () ->
                    UserChapterProgress.builder()
                        .user(user)
                        .chapter(firstChapter)
                        .completedStatus(IsCompleted.FALSE)
                        .build());
    // Mở khóa chapter đầu tiên
    userChapterProgress.setLockedStatus(IsLocked.FALSE_LOCKED);
    userChapterProgressRepository.save(userChapterProgress);

    // Mở TẤT CẢ lesson trong chapter đầu tiên
    lessonRepository
        .findAllByChapterIdAndStatusOrderByOrderIndexAsc(firstChapter.getId(), ContentStatus.ACTIVE)
        .forEach(
            lesson -> {
              UserLessonProgress userLessonProgress =
                  userLessonProgressRepository
                      .findByUserAndLesson(user, lesson)
                      .orElseGet(
                          () ->
                              UserLessonProgress.builder()
                                  .user(user)
                                  .lesson(lesson)
                                  .completedStatus(IsCompleted.FALSE)
                                  .build());
              userLessonProgress.setLockedStatus(IsLocked.FALSE_LOCKED);
              userLessonProgressRepository.save(userLessonProgress);
            });
  }

  @Transactional
  public void completeLesson(User user, Lesson lesson, long totalXpGained) {
    // 0. Mark lesson completed TRƯỚC — thêm dòng này
    updateLessonToCompleted(user, lesson);
    // 1. Cộng XP vào tài khoản user (null-safe)
    long currentTotalXp = user.getTotalXp() != null ? user.getTotalXp() : 0L;
    user.setTotalXp(currentTotalXp + Math.max(0, totalXpGained));
    // 2. Tính streak (chuỗi ngày học liên tiếp)
    LocalDate today = LocalDate.now();
    LocalDate yesterday = today.minusDays(1);
    LocalDate lastActivity = user.getLastActivityDate();
    if (lastActivity == null || lastActivity.isBefore(yesterday)) {
      // User chưa học bao giờ HOẶC đã nghỉ > 1 ngày → reset streak về 1
      user.setStreak(1);
    } else if (lastActivity.equals(yesterday)) {
      // Học liên tiếp hôm qua → tăng streak +1
      user.setStreak(user.getStreak() + 1);
    }
    // Nếu lastActivity = today → user đã học hôm nay rồi → streak giữ nguyên

    // 3. Cập nhật longest streak (chuỗi dài nhất lịch sử)
    if (user.getStreak() > user.getLongestStreak()) {
      user.setLongestStreak(user.getStreak());
    }
    // 4. Cập nhật ngày học cuối cùng = hôm nay
    user.setLastActivityDate(today);
    // Lưu user vào DB
    userRepository.save(user);
    // 6. Cập nhật enrollment (bản ghi đăng ký khóa học)
    enrollmentRepository
        .findByUserAndLanguage(user, lesson.getChapter().getLanguage())
        .ifPresent(
            enrollment -> {
              // Cộng XP vào enrollment
              enrollment.setCurrentXp(enrollment.getCurrentXp() + Math.max(0, totalXpGained));

              // Tính tổng số lesson ACTIVE trong language
              long totalLessons =
                  lessonRepository.countByChapterLanguageAndStatus(
                      lesson.getChapter().getLanguage(), ContentStatus.ACTIVE);

              // Đếm số lesson user đã hoàn thành
              long completedLessons =
                  userLessonProgressRepository
                      .countByUserAndLesson_Chapter_LanguageAndCompletedStatus(
                          user, lesson.getChapter().getLanguage(), IsCompleted.TRUE);

              // Tính % tiến độ — tránh chia cho 0 khi chưa có lesson nào
              double percentage =
                  totalLessons > 0 ? (double) completedLessons / totalLessons * 100 : 0.0;
              enrollment.setProgressPercentage(percentage);

              // Nếu hoàn thành 100% → đánh dấu enrollment hoàn thành
              if (totalLessons > 0 && percentage >= 100) {
                enrollment.setCompletedAt(LocalDateTime.now());
                enrollment.setStatus(com.example.identity_servive.enums.Status.IS_COMPLETE);
              }
              // Lưu enrollment
              enrollmentRepository.save(enrollment);

              long totalInChapter =
                  lessonRepository.countByChapterIdAndStatus(
                      lesson.getChapter().getId(), ContentStatus.ACTIVE);
              long completedInChapter =
                  userLessonProgressRepository.countByUserAndLesson_ChapterAndCompletedStatus(
                      user, lesson.getChapter(), IsCompleted.TRUE);
              if (totalInChapter > 0 && totalInChapter == completedInChapter) {
                updateChapterToCompleted(user, lesson.getChapter());
              }
            });
    long currentXp =
        enrollmentRepository
            .findByUserAndLanguage(user, lesson.getChapter().getLanguage())
            .map(Enrollment::getCurrentXp)
            .orElse(0L);
    Chapter nextChapter =
        chapterRepository
            .findFirstByLanguageAndStatusAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                lesson.getChapter().getLanguage(),
                ContentStatus.ACTIVE,
                lesson.getChapter().getOrderIndex())
            .orElse(null);
    if (nextChapter == null) return;
    boolean alreadyUnlocked =
        userChapterProgressRepository
            .findByUserAndChapter(user, nextChapter)
            .map(p -> IsLocked.FALSE_LOCKED.equals(p.getLockedStatus()))
            .orElse(false);

    if (alreadyUnlocked) {
      return;
    }
    if (currentXp >= nextChapter.getTotalXp()) {
      unlockNextChapter(user, nextChapter);
    }
  }

  @Transactional
  public void resetProgress(String languageName) {
    User user = getCurrentUser();
    Language language =
        languageRepository
            .findByNameAndStatus(languageName, ContentStatus.ACTIVE)
            .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_EXISTED));

    submissionRepository.deleteAllByUserAndLesson_Chapter_Language(user, language);
    userChapterProgressRepository.deleteAllByUserAndChapter_Language(user, language);
    userLessonProgressRepository.deleteAllByUserAndLesson_Chapter_Language(user, language);

    enrollmentRepository
        .findByUserAndLanguage(user, language)
        .ifPresent(
            enrollment -> {
              enrollment.setCurrentXp(0);
              enrollment.setProgressPercentage(0);
              enrollment.setStatus(null);
              enrollmentRepository.save(enrollment);
            });
  }
}
