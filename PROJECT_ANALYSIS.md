# AppCodeTest - Requirements Breakdown (V3 Updated)

## ĐÃ CÓ (Backend)

| Module | Status | Files |
|--------|--------|-------|
| Auth (login, register, JWT) | ✅ | AuthenticationController, Service |
| User management | ✅ | UserControler, UserService |
| Course/Lesson/Chapter (cần chuyển sang Language) | ⚠️ | CourseController, CourseService |
| Step (THEORY, QUIZ, FILL, CODE) | ✅ | LearningController, LearningService |
| Enrollment & Progress | ✅ | EnrollmentController, EnrollmentService |
| AI Chat | ✅ | ChatController, ChatService |
| Grading (Judge0) | ✅ | GradingController, GradingService |
| Role/Permission | ✅ | RoleController, PermissionController |

---

## THAY ĐỔI QUAN TRỌNG TỪ V3

| Thay đổi | Mô tả |
|----------|-------|
| Course → Language | `/api/v1/courses` → `/api/v1/languages` |
| User model mở rộng | Thêm bio, subscription, verified, maxStreak, badges |
| UserProgress | Thêm lastStepId, currentStepIndex, progressPercent |
| Daily Quest | Hệ thống quest hàng ngày |
| Subscription | FREE, PREMIUM, PRO |
| Search | Search user, lesson, problem, post, language |
| Tag | Hashtag support (#python, #java) |
| CodeRunHistory | Lưu lịch sử chạy code |
| LearningPath | Gộp nhiều language thành path |
| UserPreference | Target role, favorite language |

---

## CẦN BỔ SUNG - BACKEND

| Module | Mô tả | Priority |
|-------|-------|----------|
| **Code Runner** | Nhận code → Docker sandbox → output | HIGH |
| **Practice Problem** | CRUD problems (LeetCode-style) | HIGH |
| **Submission** | Lưu code + status, runtime, memory | HIGH |
| **TestCase** | Test cases cho problem | HIGH |
| **Language** | Thay thế Course (cần migrate) | HIGH |
| **Daily Quest** | Quest hàng ngày | MEDIUM |
| **Subscription** | FREE/PREMIUM/PRO | MEDIUM |
| **Community/Post** | Tạo post, feed, trending | MEDIUM |
| **Comment** | Comment trên post | MEDIUM |
| **Like/Save/Follow** | Like post, save problem, follow user | MEDIUM |
| **XP/Coin System** | Cộng XP/coin khi hoàn thành | MEDIUM |
| **Achievement/Badge** | Achievement badges | MEDIUM |
| **Ranking** | Leaderboard, user ranking | MEDIUM |
| **Streak** | Track daily streak + maxStreak | MEDIUM |
| **Search System** | Search user/lesson/problem/post | MEDIUM |
| **Tag System** | Hashtag support | LOW |
| **Report** | Report post/user | LOW |
| **Event** | Sự kiện đặc biệt | LOW |
| **Real-time** | WebSocket cho notification, chat | LOW |
| **Refresh Token** | Token refresh mechanism | LOW |
| **Rate Limiting** | Giới hạn request/second | LOW |

---

## MODELS CẦN TẠO/MỚI

| Model | Mô tả |
|-------|-------|
| Language | Thay thế Course |
| Chapter | Giữ nguyên, đổi courseId → languageId |
| LearningPath | Gộp nhiều language |
| CodingProblem | Problem cho practice |
| TestCase | Test case |
| Submission | Code submission |
| Post | Community post |
| Comment | Comment |
| Follow | Follow relationship |
| SavedPost | Saved posts |
| Report | Báo cáo vi phạm |
| Notification | Thông báo |
| DailyQuest | Quest hàng ngày |
| Achievement | Achievement |
| Badge | Badge |
| CodeRunHistory | Lịch sử chạy code |
| UserPreference | User preference |
| Event | Sự kiện |
| AIChatMessage | Chat history với AI |

---

## API CẦN THÊM

```http
# Language (thay Course)
GET    /api/v1/languages           # Danh sách ngôn ngữ
GET    /api/v1/languages/{id}    # Chi tiết
GET    /api/v1/languages/{id}/chapters

# Practice
POST   /api/v1/code/run           # Chạy code
GET    /api/v1/problems         # Danh sách problems
GET    /api/v1/problems/{id}    # Chi tiết problem
POST   /api/v1/submissions     # Submit code
GET    /api/v1/submissions     # Lịch sử submit

# Community
POST   /api/v1/posts            # Tạo post
GET    /api/v1/posts           # Get feed
GET    /api/v1/posts/{id}      # Get post detail
POST   /api/v1/posts/{id}/comments   # Comment
POST   /api/v1/posts/{id}/like       # Like
POST   /api/v1/posts/{id}/save       # Save
DELETE /api/v1/posts/{id}/like        # Unlike
DELETE /api/v1/posts/{id}/save      # Unsave
POST   /api/v1/users/{id}/follow     # Follow
DELETE /api/v1/users/{id}/follow    # Unfollow

# Gamification
GET    /api/v1/ranking           # Leaderboard
GET    /api/v1/achievements    # Achievements
GET    /api/v1/profile/stats   # XP, coin, streak
GET    /api/v1/quests/daily    # Daily quests

# Search & Tag
GET    /api/v1/search?q={query}&type={user|lesson|problem|post}
GET    /api/v1/tags/{tag}      # Posts by tag

# Admin
GET    /api/v1/admin/users     # Quản lý user
GET    /api/v1/admin/reports # Danh sách báo cáo
POST   /api/v1/admin/users/{id}/ban
POST   /api/v1/admin/users/{id}/mute

# Real-time
WS     /ws/notification      # WebSocket
```

---

## THỨ TỰ ĐỀ XUẤT

### Giai đoạn 1: Core (2-3 tuần)
1. Migrate Course → Language
2. Code Runner API
3. Problem CRUD + Submission

### Giai đoạn 2: Community (2 tuần)
4. Post/Comment
5. Like/Save/Follow

### Giai đoạn 3: Gamification (2 tuần)
6. XP/Coin/Level
7. Achievement/Badge
8. Ranking
9. Daily Quest

### Giai đoạn 4: Enhanced (2 tuần)
10. Search + Tag
11. Subscription
12. Refresh Token

### Giai đoạn 5: Real-time
13. WebSocket
14. Notification

---

## USER MODEL MỚI

```java
public class User {
    String id;
    String username;
    String email;
    String avatarUrl;
    String bio;              // NEW
    String role;             // USER/MODERATOR/ADMIN/SUPER_ADMIN
    String subscription;     // NEW: FREE/PREMIUM/PRO
    String status;          // ACTIVE/BANNED/DISABLED/MUTED/PENDING
    boolean verified;
    int xp;
    int coin;
    int streak;
    int maxStreak;          // NEW
    int followerCount;
    int followingCount;
    int solvedProblemCount;   // NEW
    int completedLessonCount; // NEW
    List<String> badges;     // NEW
    String createdAt;
    String updatedAt;
    String lastOnlineAt;
}
```

---

## USER PROGRESS MODEL MỚI

```java
public class UserProgress {
    String userId;
    String lessonId;
    String lastStepId;         // NEW
    int currentStepIndex;       // NEW
    float progressPercent;     // NEW
    boolean completed;
    String updatedAt;
}
```

---

## PRODUCTION MINDSET

> **Mục tiêu quan trọng nhất**: User mở app mỗi ngày (retention)

Cần tập trung:
- Streak system hấp dẫn
- Daily quest
- Notification thông minh
- AI recommendation
- Ranking realtime
- Event đặc biệt
- Badge motivation