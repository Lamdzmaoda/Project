# HỆ THỐNG APPCODE - IDENTITY SERVICE

## 1. TỔNG QUAN DỰ ÁN

**AppCodeTest** là ứng dụng học lập trình trên mobile với đầy đủ tính năng:
- **Learning System**: Học lý thuyết theo cấu trúc Language → Chapter → Lesson → Step
- **Practice System**: Luyện code với Judge0 (LeetCode-style problems)
- **AI Tutor**: Chat với AI (Gemini) để được giải thích, gợi ý code
- **Community**: Mạng xã hội với Post, Comment, Like, Follow, Saved Post
- **Gamification**: XP, Level, Streak, Coin, Ranking
- **Authentication**: JWT, Google OAuth, Role/Permission

---

## 2. TECH STACK

| Thành phần | Công nghệ |
|------------|-----------|
| **Ngôn ngữ** | Java 21 |
| **Framework** | Spring Boot 4.0.3 |
| **Build tool** | Maven 3.9.x |
| **Database** | MySQL 8.0 (production), H2 (test) |
| **ORM** | JPA / Hibernate |
| **Migration** | Flyway |
| **Security** | Spring Security, JWT (HS512), OAuth2 Resource Server |
| **AI** | Spring AI + Gemini (model: gemini-3-flash-preview) |
| **Code Runner** | Judge0 (Docker sandbox) |
| **Image Upload** | Cloudinary |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) |
| **Mapping** | MapStruct |
| **Testing** | JUnit 5, Testcontainers (MySQL) |
| **Code Coverage** | JaCoCo |
| **Code Format** | Spotless (Google Java Format) |
| **HTTP Client** | OpenFeign |
| **Container** | Docker |



## 3. CẤU TRÚC DỰ ÁN

```
identity-service/
├── src/main/java/com/example/identity_servive/
│   ├── IdentityServiveApplication.java
│   ├── configuration/
│   │   ├── ApplicationInitConfig.java      # Khởi tạo admin mặc định
│   │   ├── CloudinaryConfig.java            # Config Cloudinary
│   │   ├── CustomJwtDecoder.java            # JWT introspection
│   │   ├── JwtAuthenticationEntryPoint.java # 401 handler
│   │   ├── SecurityConfig.java             # Security filter chain, CORS
│   │   ├── SwaggerConfig.java              # OpenAPI/Swagger
│   │   └── TransactionConfig.java          # JPA transaction
│   ├── constant/
│   │   └── PredefinedRole.java             # USER, ADMIN constants
│   ├── controller/
│   │   ├── auth/
│   │   │   ├── AuthenticationController.java  # Login, Register, OAuth
│   │   │   ├── PermissionController.java      # CRUD permissions
│   │   │   ├── RoleController.java            # CRUD roles
│   │   │   └── UserControler.java             # User CRUD, avatar
│   │   ├── community/
│   │   │   ├── AdminCommunityController.java  # Admin xóa post/comment
│   │   │   └── CommunityController.java       # Post, Comment, Like, Follow
│   │   ├── learning/
│   │   │   ├── CourseController.java          # CRUD Language/Chapter/Lesson/Step/Problem
│   │   │   ├── EnrollmentController.java      # Ghi danh khóa học
│   │   │   ├── PistonAPIController.java       # Code execution API
│   │   │   └── UserLearningController.java    # Học tập, verify, progress
│   │   ├── ChatController.java                # AI chat
│   │   └── GradingController.java             # (Commented) Grading
│   ├── dto/request/
│   │   ├── ai/ (ChatRequest, CodeRequest, PythonRequest)
│   │   ├── AuthRequest/ (Authentication, ExchangeToken, Introspect, Logout, Refresh, Permission, Role, UserCreation, UserUpdatePassword, UserUpdateRequest)
│   │   ├── community/ (CommentRequest, PostRequest)
│   │   └── learningRequest/ (Chapter, Language, Lesson, Problem, Step, Enrollment, PracticeSubmit, Verify, ...)
│   ├── dto/response/ (ApiResponse, AuthResponse, CommunityResponse, LearningResponse, ProgressResponse, ...)
│   ├── entity/
│   │   ├── AI/OpenAI.java                    # Chat history
│   │   ├── auth/ (User, Role, Permission, InvalidatedToken)
│   │   ├── community/ (Post, Comment, PostLike, SavedPost, Follow)
│   │   ├── learning/ (Language, Chapter, Lesson, Step, Problem, ProblemCondition)
│   │   ├── progress/ (Enrollment, Submission, UserChapterProgress, UserLessonProgress)
│   │   └── System/Code.java                  # Code execution records
│   ├── enums/ (ContentStatus, Difficulty, IsCompleted, IsLocked, LessonType, Role, Status, Type)
│   ├── exception/ (AppException, ErrorCode, GlobalExceptionHandler)
│   ├── mapper/ (ChatMapper, CodeMapper, CommentMapper, CourseMapper, EnrollmentMapper, ...)
│   ├── repository/ (21 repositories)
│   ├── service/
│   │   ├── ai/ (ChatService, ChatServiceTest, GradingService)
│   │   ├── auth/ (AuthenticationService, PermissionService, RoleService, UserSecurity, UserService)
│   │   ├── cloudinary/CloudinaryService.java
│   │   ├── community/ (CommentService, FollowService, PostLikeService, PostService, SavedPostService)
│   │   ├── learning/ (CourseService, EnrollmentService, LearningProgressService, LearningService, PracticeService)
│   │   └── system/Judge0APIService.java
│   └── validator/ (DobConstraint, DobValidator)
├── src/main/resources/
│   ├── application.yaml
│   └── db/migration/V1__.sql
├── src/test/ (...)
├── diagrams/ (16 .puml + .png files)
├── pom.xml
├── Dockerfile
├── judge0/ (Judge0 Docker config)
├── postman-tests/
├── PROJECT_ANALYSIS.md
├── BAO_CAO.md
├── readme.MD
└── generate_report.py
```



## 4. CHI TIẾT CÁC MODULE

### 4.1 AUTHENTICATION MODULE
**Controllers:** `AuthenticationController`, `UserControler`, `RoleController`, `PermissionController`
**Services:** `AuthenticationService`, `UserService`, `RoleService`, `PermissionService`, `UserSecurity`

**Chức năng:**
- **Register**: Tạo user mới (username, password, email, birthDate)
- **Login**: POST /auth/token → JWT access token (1h) + refresh token
- **Introspect**: POST /auth/introspect → kiểm tra token còn hạn không
- **Refresh**: POST /auth/refresh → cấp access token mới (refresh token có hạn 100h)
- **Logout**: POST /auth/logout → đưa token vào blacklist
- **Google OAuth**: POST /auth/outbound/authentication?code=... → đăng nhập bằng Google
- **User CRUD**: GET/PUT users, update password, upload avatar
- **Role CRUD**: Tạo role, gán permission
- **Permission CRUD**: Tạo permission (name, description)

**Security:**
- JWT HS512 với signer key
- Spring Security filter chain
- Endpoint public: /auth/token, /auth/introspect, /auth/logout, /auth/refresh, /auth/outbound/authentication
- Role-based: USER, ADMIN mặc định
- Owner check: UserSecurity kiểm tra quyền sở hữu



### 4.2 LEARNING SYSTEM (Course)
**Controller:** `CourseController`, `UserLearningController`, `EnrollmentController`
**Services:** `CourseService`, `EnrollmentService`, `LearningProgressService`, `LearningService`, `PracticeService`

**Cấu trúc nội dung:**
```
Language (ngôn ngữ lập trình: Python, Java, ...)
  └── Chapter (chương: Biến, Vòng lặp, ...)
       └── Lesson (bài học)
            ├── Step (bước học: THEORY, QUIZ, CODE, TEXT)
            └── Problem (bài tập code)
                 └── ProblemCondition (điều kiện code mong đợi)
```

**API CRUD (CourseController):**
- Languages: POST/GET/PUT/DELETE + purge (xóa cứng)
- Chapters: POST/GET/PUT/DELETE + purge
- Lessons: POST/GET/PUT/DELETE + purge
- Steps: POST/GET/PUT/DELETE + purge
- Problems: POST/GET/PUT/DELETE + purge

**API User Learning (UserLearningController):**
- GET /user/languages → danh sách language cho user
- GET /user/languages/{name}/chapters → chapters đã unlock
- GET /user/chapters/{chapterId}/lessons → lessons đã unlock
- GET /user/lessons/{lessonId}/steps → steps + problem
- POST /user/lessons/{lessonId}/verify → kiểm tra đáp án step
- POST /user/lessons/{lessonId}/complete → hoàn thành lesson
- POST /user/lessons/practice/{lessonId}/submit → submit code practice
- GET /user/progress → tiến độ học tập

**API Enrollment:**
- POST /enrollments → ghi danh vào language
- GET /enrollments → danh sách enrollment của user

**Step Types (enum Type):**
- THEORY: Nội dung lý thuyết (JSON data chứa content)
- QUIZ: Câu hỏi trắc nghiệm
- CODE: Yêu cầu viết code
- TEXT: Văn bản

**Lesson Types (enum LessonType):**
- LEARN: Bài học thông thường
- PRACTICE: Bài luyện tập code

**Progress Tracking:**
- UserLessonProgress: locked/completed status per lesson
- UserChapterProgress: locked/completed + progress percentage per chapter
- Enrollment: currentXp, progressPercentage, enrolledAt, completedAt
- XP system: User.totalXp, User.getLevel() = totalXp/1000 + 1



### 4.3 AI CHAT MODULE
**Controller:** `ChatController`
**Services:** `ChatService`, `ChatServiceTest`

**Chức năng:**
- POST /chats → Gửi message cho AI (Gemini)
- GET /chats → Lấy toàn bộ lịch sử chat
- GET /chats/user/{userId} → Lấy lịch sử chat của user
- POST /ai/chat → Chat theo lesson (step context)

**AI Personality:** "Gojo" - phong cách Nhật Bản, hơi ngầu, nhiệt tình
- Hỗ trợ 4 chế độ:
  - Giải thích code (Explain code)
  - Gợi ý (Hint)
  - Động viên (Motivation)
  - Sửa lỗi (Fix code)
- Conversation memory: Spring AI JDBC chat memory

**Entity OpenAI:**
- userMessage, aiExplanation, hints (List), suggestedCode, motivationMessage
- Liên kết với User và Step



### 4.4 CODE GRADING (Judge0)
**Controller:** `PistonAPIController`, `GradingController` (commented)
**Services:** `PracticeService`, `Judge0APIService`

**Chức năng:**
- POST /piston-api/execute → Chạy code Python qua Judge0
- POST /grading/submit → Submit code chấm điểm
- POST /user/lessons/practice/{lessonId}/submit → Submit practice

**Judge0 Flow:**
1. User gửi code
2. Gọi Judge0 API (Docker sandbox)
3. Nhận kết quả: stdout, stderr, runtime, memory
4. Parse lỗi: errorType, line, column, messageVn (tiếng Việt)
5. So sánh output với expectedOutput → passed/failed
6. Lưu Submission + Code execution record

**Entity Code:**
- language, status, errorType, line, columnIndex, errorLineCode, pointer, messageVn, input, output



### 4.5 COMMUNITY MODULE
**Controller:** `CommunityController`, `AdminCommunityController`
**Services:** `PostService`, `CommentService`, `PostLikeService`, `FollowService`, `SavedPostService`, `CloudinaryService`

**Chức năng:**
- **Posts:** CRUD, feed (pagination), following feed, user posts, upload image
- **Comments:** CRUD, get by post, replies (parent_id)
- **Likes:** Like/unlike, check liked status
- **Follows:** Follow/unfollow, get followers/following, counts, check
- **Saved Posts:** Save/unsave, get saved list
- **Admin:** Delete any post/comment



### 4.6 CLOUDINARY
**Service:** `CloudinaryService`
- Upload ảnh lên Cloudinary
- Dùng cho: avatar user, ảnh bài viết community



---

## 5. DATABASE DESIGN

### 5.1 Danh sách bảng

| Bảng | Mô tả |
|------|-------|
| **users** | id (UUID), username (unique), password, email, displayName, avatarUrl, bio, coin, longestStreak, verified, status (ACTIVE/BANNED/DISABLED), streak, lastActivityDate, birthDate, totalXp, createdAt, updatedAt |
| **role** | name (PK), description |
| **permission** | name (PK), description |
| **users_roles** | user_id, roles_name |
| **roles_permissions** | role_name, permissions_name |
| **invalidated_token** | id (PK), expiryTime |
| **language** | language_name (PK), description, icon, durationDays, totalXp, slug, status, createAt, updateAt |
| **chapter** | id (UUID), title, orderIndex, description, slug, totalXp, status, language_id (FK), createAt, updateAt; **UK(language_id, orderIndex)** |
| **lesson** | id (UUID), title, description, slug, thumbnailUrl, orderIndex, xp, lesson_type, status, chapter_id (FK), createAt, updateAt; **UK(chapter_id, orderIndex)** |
| **step** | id (UUID), orderIndex, title, type (THEORY/QUIZ/CODE/TEXT), status, data (JSON), lesson_id (FK), createAt, updateAt; **UK(lesson_id, orderIndex)** |
| **step_data** | id, step_id, correctValue, content *(từ chuẩn hóa 1NF)* |
| **problem** | id (UUID), title, slug, description (TEXT), difficulty, hint (TEXT), methodName, orderIndex, solutionCode (TEXT), expectedOutput, language_id (FK), lesson_id (FK), status, createAt, updateAt; **UK(lesson_id, orderIndex)** |
| **problem_condition** | id (UUID), problem_id (FK), expectedCode (TEXT), hint (TEXT), orderIndex |
| **enrollment** | id (UUID), currentXp, status, progressPercentage, enrolledAt, completedAt, user_id (FK), language_id (FK), createdAt, updatedAt; **UK(user_id, language_id)** |
| **submission** | id (UUID), user_id (FK), lesson_id (FK), problem_id (FK), code (LONGTEXT), output (TEXT), passed, createdAt |
| **user_lesson_progress** | id (UUID), user_id (FK), lesson_id (FK), is_locked, is_completed, createdAt, updatedAt |
| **user_chapter_progress** | id (UUID), user_id (FK), chapter_id (FK), progressPercentage, is_locked, is_completed, createdAt, updatedAt |
| **post** | id (UUID), user_id (FK), content (LONGTEXT), imageUrl, codeSnippet (LONGTEXT), likeCount, commentCount, createdAt, updatedAt |
| **comment** | id (UUID), post_id (FK), user_id (FK), parent_id (FK → comment), content (LONGTEXT), createdAt |
| **post_like** | id (UUID), user_id (FK), post_id (FK), createdAt; **UK(post_id, user_id)** |
| **saved_post** | id (UUID), user_id (FK), post_id (FK), savedAt; **UK(user_id, post_id)** |
| **follow** | id (UUID), follower_id (FK), followee_id (FK), createdAt; **UK(follower_id, followee_id)** |
| **openai** | id (UUID), user_id (FK), step_id (FK), userMessage (TEXT), aiExplanation (TEXT), hints (ElementCollection), suggestedCode (TEXT), motivationMessage, createAt |
| **code** | id (UUID), language, status, errorType, line, columnIndex, errorLineCode, pointer, messageVn, input, output |



### 5.2 Mối quan hệ

```
User ──< Role ──< Permission
User ──< Enrollment >── Language
Language ──< Chapter ──< Lesson ──< Step
Lesson ──< Problem ──< ProblemCondition
User ──< UserLessonProgress >── Lesson
User ──< UserChapterProgress >── Chapter
User ──< Post ──< Comment
Post ──< PostLike >── User
Post ──< SavedPost >── User
User ──< Follow >── User (follower/followee)
User ──< Submission >── Problem
User ──< OpenAI >── Step
User ──< Code
```



### 5.3 Chuẩn hóa

**1NF:** Đã chuẩn hóa - tách Step.data (JSON) thành bảng step_data, loại bỏ trường tính toán (likeCount, commentCount)
**2NF:** Tất cả bảng đều có khóa chính đơn (UUID) → không có phụ thuộc hàm bộ phận → thỏa 2NF



## 6. API ENDPOINTS

### Auth
```
POST /auth/token                    # Login
POST /auth/introspect               # Kiểm tra token
POST /auth/refresh                  # Refresh token
POST /auth/logout                   # Logout
POST /auth/outbound/authentication  # Google OAuth
```

### Users
```
GET    /users/me                    # Thông tin user hiện tại
GET    /users/{userId}              # Thông tin user khác
PUT    /users/me                    # Cập nhật profile
PUT    /users/me/password           # Đổi mật khẩu
POST   /users/avatar                # Upload avatar
GET    /users/suggestions           # Gợi ý bạn bè
GET    /users/search?q=             # Tìm kiếm user
```

### Roles & Permissions
```
POST   /roles                       # Tạo role
GET    /roles                       # Danh sách role
PUT    /roles/{role}                # Cập nhật role
DELETE /roles/{role}                # Xóa role
POST   /permissions                 # Tạo permission
GET    /permissions                 # Danh sách permission
DELETE /permissions/{permission}    # Xóa permission
```

### Course (Learning Content)
```
POST   /course/languages            # Tạo language
GET    /course/languages            # Danh sách language
GET    /course/languages/{name}     # Chi tiết language
PUT    /course/languages/{name}     # Cập nhật language
DELETE /course/languages/{name}     # Soft-delete language
DELETE /course/languages/admin/{name}  # Purge language

POST   /course/chapters             # Tạo chapter
GET    /course/chapters/{id}        # Chi tiết chapter
GET    /course/chapters/admin/{languageName}  # Chapters theo language (admin)
PUT    /course/chapters/{id}        # Cập nhật chapter
DELETE /course/chapters/{id}        # Soft-delete
DELETE /course/chapters/admin/{id}  # Purge

POST   /course/lessons              # Tạo lesson
GET    /course/lessons/{id}         # Chi tiết lesson
GET    /course/lessons/admin/{chapterId}  # Lessons theo chapter (admin)
PUT    /course/lessons/{id}         # Cập nhật lesson
DELETE /course/lessons/{id}         # Soft-delete
DELETE /course/lessons/admin/{id}   # Purge

POST   /course/steps                # Tạo step
GET    /course/steps/{id}           # Chi tiết step
GET    /course/steps/admin/{lessonId}  # Steps theo lesson (admin)
PUT    /course/steps/{id}           # Cập nhật step
DELETE /course/steps/{id}           # Soft-delete
DELETE /course/steps/admin/{id}     # Purge

POST   /course/problems             # Tạo problem
GET    /course/problems/{id}        # Chi tiết problem
GET    /course/problems/admin/{lessonId}  # Problems theo lesson
PUT    /course/problems/{id}        # Cập nhật problem
DELETE /course/problems/{id}        # Soft-delete
DELETE /course/problems/admin/{id}  # Purge
```

### User Learning
```
GET    /user/languages                      # Languages sẵn có
GET    /user/languages/{name}/chapters      # Chapters đã unlock
GET    /user/chapters/{chapterId}/lessons   # Lessons đã unlock
GET    /user/lessons/{lessonId}/steps       # Steps + problem
POST   /user/lessons/{lessonId}/verify      # Kiểm tra đáp án step
POST   /user/lessons/{lessonId}/complete    # Hoàn thành lesson
POST   /user/lessons/practice/{lessonId}/submit  # Submit practice code
GET    /user/progress                       # Tiến độ học tập
```

### Enrollment
```
POST   /enrollments              # Ghi danh vào language
GET    /enrollments              # Danh sách ghi danh
```

### AI Chat
```
POST   /chats                    # Gửi tin nhắn AI
GET    /chats                    # Lịch sử chat
GET    /chats/user/{userId}      # Lịch sử của user
POST   /ai/chat                  # Chat với context lesson
```

### Code Execution
```
POST   /piston-api/execute       # Chạy code Python
POST   /grading/submit           # Submit chấm điểm
```

### Community
```
POST   /api/community/posts                    # Tạo bài viết
GET    /api/community/posts                    # Feed (pagination)
GET    /api/community/posts/{postId}           # Chi tiết bài viết
GET    /api/community/users/{userId}/posts     # Bài viết của user
DELETE /api/community/posts/{postId}           # Xóa bài viết
GET    /api/community/feed/following           # Feed người đang follow

POST   /api/community/comments                 # Tạo bình luận
GET    /api/community/posts/{postId}/comments  # Bình luận của bài
DELETE /api/community/comments/{commentId}     # Xóa bình luận

POST   /api/community/posts/{postId}/like      # Like
DELETE /api/community/posts/{postId}/like      # Unlike
GET    /api/community/posts/{postId}/liked     # Check liked

POST   /api/community/follow/{followeeId}      # Follow
DELETE /api/community/follow/{followeeId}      # Unfollow
GET    /api/community/users/{userId}/following         # Danh sách đang follow
GET    /api/community/users/{userId}/followers         # Follower
GET    /api/community/users/{userId}/following/count   # Số lượng following
GET    /api/community/users/{userId}/followers/count   # Số lượng follower
GET    /api/community/follow/{followeeId}/check        # Check follow status

POST   /api/community/posts/{postId}/save      # Lưu bài viết
DELETE /api/community/posts/{postId}/save      # Bỏ lưu
GET    /api/community/saved-posts              # Bài viết đã lưu
GET    /api/community/posts/{postId}/saved     # Check saved

POST   /api/community/upload-image             # Upload ảnh community
```



## 7. ENUMS

```java
// ContentStatus: ACTIVE, INACTIVE, DRAFT
// Difficulty: EASY, MEDIUM, HARD
// IsCompleted: TRUE, FALSE
// IsLocked: TRUE_LOCKED, FALSE_UNLOCKED
// LessonType: LEARN, PRACTICE
// Role: USER, ADMIN
// Status: ACTIVE, BANNED, DISABLED, PENDING
// Type: THEORY, QUIZ, CODE, TEXT
```



## 8. DIAGRAMS (PlantUML)

### Activity Diagrams:
| File | Nội dung |
|------|----------|
| `ad-auth.puml` | Luồng đăng ký + đăng nhập |
| `ad-admin.puml` | Quản lý nội dung (CRUD Language/Chapter/Lesson/Step, gồm soft-delete/purge) |
| `ad-learning.puml` | Học tập: enrollment → học lesson → complete (có verify) |
| `ad-practice.puml` | Luyện tập: submit code → Judge0 chấm → kết quả |
| `ad-chat.puml` | Chat AI: gửi message → AI trả lời → lưu history |
| `diagrams/activity-login.puml` | Login flow với lockout logic |
| `diagrams/activity-diagram.puml` | Auth API flow tổng thể với JWT validation |

### Use Case Diagrams:
| File | Nội dung |
|------|----------|
| `use-case-diagram.puml` | Tổng thể 21 use cases |
| `use-case-compact.puml` | Phiên bản compact |
| `uc-auth.puml` | Auth: register, login, OAuth, roles, permissions |
| `uc-course.puml` | Course CRUD chi tiết |
| `uc-learning.puml` | Learning flow |
| `uc-ai-system.puml` | AI Tutor + System (Judge0, submissions) |
| `uc-social-detail.puml` | Community & Gamification |
| `uc-practice-detail.puml` | AI Tutor & Coding practice |
| `uc-learning-detail.puml` | Learning system detail |
| `uc-auth-detail.puml` | Auth & user management detail |
| `uc-tong-quan.puml` | Tổng quan hệ thống |
| `uc-features.puml` | Feature overview |

### Other Diagrams:
| File | Nội dung |
|------|----------|
| `diagrams/class-diagram.puml` | Full class diagram (317 lines) |
| `diagrams/sequence-diagram.puml` | Login + API access sequence |
| `diagrams/communication-diagram.puml` | Post creation communication |
| `diagrams/component-diagram.puml` | Component architecture |
| `diagrams/deployment-diagram.puml` | Deployment: Client → Docker → MySQL → Cloud |
| `diagrams/state-machine-diagram.puml` | State machines: User, Content, Enrollment, Progress, Submission, JWT, AI Chat |
| `diagrams/object-diagram.puml` | Object instance with sample data |
| `diagrams/package-diagram.puml` | Package structure |
| `diagrams/permission-matrix.puml` | Role permissions matrix |



## 9. CONFIGURATION

### application.yaml key settings:
- **Port:** 8080, **Context-path:** /identity
- **Database:** MySQL port 3636 (Docker), credentials qua env vars
- **AI:** Gemini model gemini-3-flash-preview, base URL: generativelanguage.googleapis.com
- **JWT:** HS512, access token 1h, refresh token 100h
- **JPA:** ddl-auto=update, show-sql=true, MySQL dialect
- **Flyway:** enabled, location classpath:db/migration
- **Swagger:** /swagger-ui.html
- **Cloudinary:** cloud-name, api-key, api-secret (env vars)
- **Google OAuth:** client-id, client-secret, redirect-uri

### Docker:
- Multi-stage build: Maven 3.9.14 + Corretto 25 → JAR → Alpine Corretto 25
- Judge0 image: judge0/compilers:1.4.0

### Tests:
- H2 in-memory DB cho test
- Testcontainers MySQL
- Test coverage: AuthController, UserController, AuthService, UserService



## 10. TEST COVERAGE

**Files test:**
| File | Loại |
|------|------|
| `AuthenticationControllerTest.java` | Unit test (mock) |
| `UserControllerTest.java` | Unit test (mock) |
| `AuthenticationServiceTest.java` | Unit test (mock) |
| `UserServiceTest.java` | Unit test (mock) |
| `UserControllerIntegrationTest.java` | Integration (Testcontainers) |
| `IdentityServiveApplicationTests.java` | Hash test |

**JaCoCo excludes:** dto, entity, mapper, configuration packages



## 11. HIỆN TRẠNG PHÁT TRIỂN

### ✅ Đã hoàn thành:
1. Auth + JWT + Google OAuth
2. User CRUD + Role/Permission
3. Learning System (Language → Chapter → Lesson → Step/Problem)
4. Enrollment + Progress tracking
5. AI Chat (Gemini)
6. Code Grading (Judge0 integration)
7. Community (Post, Comment, Like, Follow, Saved Post)
8. Image upload (Cloudinary)
9. Soft-delete + Purge cho nội dung học tập
10. Swagger API docs
11. Docker build
12. Flyway migration

### ⚠️ Đang phát triển / Cần bổ sung:
1. **Migrate Course → Language** (đang dùng lẫn /course và /user endpoints)
2. **Code Runner API** (PistonAPIController đang hoạt động, cần hoàn thiện)
3. **Practice Problem System** (đã có entity Problem, cần API public)
4. **Gamification**:
   - Daily Quest
   - Achievement/Badge
   - Ranking/Leaderboard
5. **Community bổ sung**:
   - Report system
   - Trending feed
6. **Search + Tag system**
7. **Subscription (FREE/PREMIUM/PRO)**
8. **Refresh Token enhancement**
9. **WebSocket Real-time (notification, chat)**
10. **Rate Limiting**



## 12. TESTING & CI

- **JaCoCo** code coverage (exclude DTO, entity, mapper, config)
- **Spotless** code format check (Google Java Format)
- **Testcontainers** MySQL for integration tests
- **H2** in-memory DB for unit tests

Test commands:
```bash
mvn test                    # Chạy test
mvn clean package           # Build + test
mvn jacoco:report           # Báo cáo coverage
```
