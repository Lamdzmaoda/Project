# Báo cáo Dự án AppCodeTest - Backend Status

> Ngày báo cáo: 07/05/2026

---

## 1. TỔNG QUAN DỰ ÁN

**AppCodeTest** là ứng dụng học lập trình trên mobile với các tính năng:
- Học lý thuyết (Learning)
- Luyện code thật (Practice)
- AI Tutor
- Mạng xã hội (Community)
- Gamification

**Tech Stack Backend:**
- Spring Boot 4.0.3
- Java 25
- MySQL / PostgreSQL
- Spring Security + JWT
- Spring AI (OpenAI integration)
- Judge0 (code grading)

---

## 2. BACKEND ĐÃ CÓ ✅

### 2.1 Authentication
- Đăng ký / Đăng nhập
- JWT token management
- Introspect / Logout
- Role: USER, ADMIN

### 2.2 User Management
- CRUD User
- Role/Permission management

### 2.3 Learning System (Course)
```
Language → Chapter → Lesson → Step
```
- Tạo/Đọc/Cập nhật/Xóa Language, Chapter, Lesson, Step
- Step types: THEORY, QUIZ, FILL, CODE, AI_CHAT

### 2.4 Enrollment & Progress
- Ghi danh khóa học
- Theo dõi tiến độ học
- Lưu last step đã học

### 2.5 AI Chat
- Tích hợp OpenAI
- Chat trong từng lesson

### 2.6 Grading (Judge0)
- Submit code
- Check output
- Runtime, memory tracking

---

## 3. API HIỆN TẠI

```http
# Auth
POST   /api/v1/auth/login
POST   /api/v1/auth/register
POST   /api/v1/auth/introspect
POST   /api/v1/auth/logout

# User
GET    /api/v1/users/me
GET    /api/v1/users/{id}

# Language (trước đây là Course)
GET    /api/v1/course/languages
GET    /api/v1/course/languages/{name}
GET    /api/v1/course/chapters/BetterLanguage/{name}
GET    /api/v1/course/lessons/BetterChapter/{chapterId}
GET    /api/v1/course/steps/BetterLesson/{lessonId}

# Enrollment
POST   /api/v1/enrollments

# AI
POST   /api/v1/ai/chat

# Grading
POST   /api/v1/grading/submit
```

---

## 4. FRONTEND CẦN (Dựa trên Architecture V3)

| Module | Status | Nội dung |
|--------|--------|----------|
| Login/Register | ✅ Backend có | Auth API |
| Home Screen | ⚠️ Cần mapping | Language list, XP, streak |
| Learning | ✅ Backend có | Course API đang dùng |
| Practice | ❌ Chưa có | Code Runner, Problems |
| Community | ❌ Chưa có | Post, Comment |
| Profile | ⚠️ Cần mở rộng | XP, coin, badges |
| AI Chat | ✅ Backend có | AI API |
| Real-time | ❌ Chưa có | WebSocket |

---

## 5. CẦN BỔ SUNG (Theo tầm quan trọng)

### 5.1 Practice System (HIGH)
- **Code Runner API**: Nhận code → chạy trong Docker → trả output
- **Problem CRUD**: Problems như LeetCode
- **Submission**: Lưu code + status

### 5.2 Community (MEDIUM)
- Post / Comment
- Like / Save / Follow
- Trending feed

### 5.3 Gamification (MEDIUM)
- XP / Coin / Level
- Achievement / Badge
- Ranking / Leaderboard
- Streak tracking
- Daily Quest

### 5.4 Enhanced (LOW)
- Search + Tag
- Subscription (FREE/PREMIUM/PRO)
- Refresh Token
- WebSocket Real-time
- Rate Limiting

---

## 6. CẤU TRÚC PROJECT

```
identity-service/
├── src/main/java/com/example/identity_servive/
│   ├── controller/    (12 files)
│   ├── service/     (13 files)
│   ├── repository/  (17 files)
│   ├── entity/     (14 files)
│   ├── dto/       (request/response)
│   ├── mapper/    (MapStruct)
│   └── configuration/
├── src/main/resources/
│   └── application.yaml
├── pom.xml
└── target/
```

---

## 7. KẾ HOẠCH PHÁT TRIỂN

### Giai đoạn 1 (2-3 tuần)
1. Code Runner API
2. Practice Problem + Submission

### Giai đoạn 2 (2 tuần)
3. Post/Comment
4. Like/Save/Follow

### Giai đoạn 3 (2 tuần)
5. XP/Coin/Achievement
6. Ranking
7. Daily Quest

### Giai đoạn 4 (2 tuần)
8. Search + Tag
9. Subscription
10. Refresh Token

### Giai đoạn 5
11. WebSocket
12. Notification

---

## 8. KẾT LUẬN

**Đã hoàn thành:**
- ✅ Auth + Security
- ✅ Learning System cơ bản
- ✅ AI Chat integration
- ✅ Code Grading (Judge0)

**Cần phát triển thêm:**
- ❌ Practice/Code Runner
- ❌ Community
- ❌ Gamification đầy đủ
- ❌ Real-time

**Backend đang hoạt động ổn định**, có thể mở rộng theo kế hoạch trên.

---

## 3. CHUẨN HÓA

### 3.1 Chuẩn hóa 1NF

Một bảng được gọi là ở dạng 1NF nếu miền giá trị của một thuộc tính chỉ chứa giá trị nguyên tố đơn (không phân chia được) và giá trị của mỗi thuộc tính cũng là một giá trị đơn lấy từ miền giá trị của nó. Để bảng đạt chuẩn hóa dạng 1NF:
- Các thuộc tính của bảng phải là nguyên tố không phải là thuộc tính đa trị
- Giá trị của các thuộc tính trên bảng phải là đơn trị - không chứa nhóm lặp
- Không có một thuộc tính nào có giá trị có thể tính toán được từ một thuộc tính khác
- Xác định được thuộc tính khóa chính

**Các vi phạm 1NF trong hệ thống:**

1. **Step.data** — cột JSON chứa nhiều giá trị (`correctValue`, ...) trong một ô, vi phạm nguyên tắc đơn trị.
2. **Post.likeCount** / **Post.commentCount** — các trường có thể tính toán được từ bảng `PostLike` và `Comment`.

**Sau chuẩn hóa 1NF**, ta có các bảng dữ liệu như sau:

| Tên bảng | Thuộc tính |
|----------|-----------|
| **users** | id, username, password, email, displayName, avatarUrl, bio, coin, longestStreak, verified, status, streak, lastActivityDate, birthDate, totalXp, createdAt, updatedAt |
| **role** | name, description |
| **permission** | name, description |
| **invalidated_token** | id, expiryTime |
| **language** | name, description, icon, durationDays, totalXp, slug, status, createAt, updateAt |
| **chapter** | id, title, orderIndex, description, slug, totalXp, status, language_id, createAt, updateAt |
| **lesson** | id, title, description, slug, thumbnailUrl, orderIndex, xp, lessonType, status, chapter_id, createAt, updateAt |
| **step** | id, orderIndex, title, type, status, lesson_id, createAt, updateAt |
| **step_data** | id, step_id, correctValue, content |
| **problem** | id, title, slug, description, difficulty, hint, methodName, orderIndex, solutionCode, expectedOutput, language_id, lesson_id, status, createAt, updateAt |
| **problem_condition** | id, problem_id, expectedCode, hint, orderIndex |
| **enrollment** | id, currentXp, status, progressPercentage, enrolledAt, completedAt, user_id, language_id, createdAt, updatedAt |
| **user_lesson_progress** | id, user_id, lesson_id, lockedStatus, completedStatus, createdAt, updatedAt |
| **user_chapter_progress** | id, user_id, chapter_id, progressPercentage, lockedStatus, completedStatus, createdAt, updatedAt |
| **submission** | id, user_id, lesson_id, problem_id, code, output, passed, createdAt |
| **post** | id, user_id, content, imageUrl, codeSnippet, createdAt, updatedAt |
| **comment** | id, post_id, user_id, parent_id, content, createdAt |
| **post_like** | id, post_id, user_id, createdAt |
| **saved_post** | id, user_id, post_id, savedAt |
| **follow** | id, follower_id, followee_id, createdAt |
| **openai** | id, user_id, step_id, userMessage, aiExplanation, suggestedCode, motivationMessage, createAt |
| **openai_hints** | openai_id, hints |
| **code** | id, language, status, errorType, line, columnIndex, errorLineCode, pointer, messageVn, input, output |
| **users_roles** | user_id, role_id |
| **roles_permissions** | role_id, permission_id |

### 3.2 Chuẩn hóa 2NF

- Phải thỏa mãn chuẩn 1NF
- Phụ thuộc hàm đầy đủ vào khóa chính
- Với các quan hệ có tính khóa đơn thì không phải xét – chỉ kiểm tra lược đồ có chứa phụ thuộc hàm bộ phận

**Nhận xét:** Tất cả các bảng trong hệ thống đều sử dụng khóa chính đơn (UUID hoặc String), không có khóa ghép. Do đó không tồn tại phụ thuộc hàm bộ phận — **các bảng đã thỏa mãn chuẩn 2NF**, không cần tách thêm.

---

## 4. TỔNG KẾT

• Sau khi chuẩn hóa 1NF và 2NF ta có những bảng sau:

| Tên Bảng | Thuộc Tính |
|----------|-----------|
| **user** | id, username, password, email, displayName, avatarUrl, bio, coin, streak, longestStreak, verified, totalXp, birthDate, lastActivityDate, status, createdAt, updatedAt |
| **language** | languageName, description, icon, durationDays, totalXp, slug, status, createAt, updateAt |
| **chapter** | id, title, orderIndex, description, slug, totalXp, status, language_id, createAt, updateAt |
| **lesson** | id, title, description, slug, thumbnailUrl, orderIndex, xp, lessonType, status, chapter_id, createAt, updateAt |
| **step** | id, orderIndex, title, type, status, lesson_id, createAt, updateAt |
| **step_data** | id, step_id, correctValue, content |
| **problem** | id, title, slug, description, difficulty, hint, methodName, orderIndex, solutionCode, expectedOutput, language_id, lesson_id, status, createAt, updateAt |
| **problem_condition** | id, problem_id, expectedCode, hint, orderIndex |
| **enrollment** | id, currentXp, status, progressPercentage, enrolledAt, completedAt, user_id, language_id, createdAt, updatedAt |
| **user_lesson_progress** | id, user_id, lesson_id, lockedStatus, completedStatus, createdAt, updatedAt |
| **user_chapter_progress** | id, user_id, chapter_id, progressPercentage, lockedStatus, completedStatus, createdAt, updatedAt |
| **submission** | id, user_id, lesson_id, problem_id, code, output, passed, createdAt |
| **post** | id, user_id, content, imageUrl, codeSnippet, createdAt, updatedAt |
| **comment** | id, post_id, user_id, parent_id, content, createdAt |
| **post_like** | id, post_id, user_id, createdAt |
| **saved_post** | id, user_id, post_id, savedAt |
| **follow** | id, follower_id, followee_id, createdAt |
| **openai** | id, user_id, step_id, userMessage, aiExplanation, suggestedCode, motivationMessage, createAt |
| **openai_hints** | openai_id, hints |
| **code** | id, language, status, errorType, line, columnIndex, errorLineCode, pointer, messageVn, input, output |
| **role** | name, description |
| **permission** | name, description |
| **invalidated_token** | id, expiryTime |
| **users_roles** | user_id, roles_name |
| **roles_permissions** | role_name, permissions_name |

---

## 9. ĐẶC ĐIỂM NGƯỜI SỬ DỤNG

Dựa vào khảo sát, chúng tôi nhận định đặc điểm của người sử dụng hệ thống AppCodeTest gồm có:

- **Quản trị viên (Admin):**
  Sử dụng toàn bộ các chức năng của hệ thống như quản lý người dùng, quản lý khóa học/ngôn ngữ lập trình, quản lý bài tập (problems), kiểm duyệt bài viết cộng đồng, quản lý vai trò và phân quyền, xem thống kê người dùng và doanh thu từ gói thuê bao.

- **Người học (Học viên):**
  Thực hiện đăng ký tài khoản, chọn ngôn ngữ lập trình để học, xem lý thuyết và làm bài tập (quiz, điền code, lập trình), trò chuyện với AI Tutor để được hỗ trợ, tham gia cộng đồng (đăng bài, bình luận), theo dõi tiến độ học tập, tích lũy XP/coin, nhận huy hiệu (badge) và leo bảng xếp hạng.

---

## 10. YÊU CẦU HỆ THỐNG

| Thành phần | Mô tả |
|------------|-------|
| Ngôn ngữ lập trình | Back-end: Java 21, Spring Boot 4.0.3, Maven |
| Cơ sở dữ liệu | MySQL 8.0 (production), H2 in-memory (test) |
| Hệ điều hành | Cross-platform (Docker Alpine Linux, Windows, macOS, Linux) |
| RAM | Tối thiểu 2GB |
| CPU | Intel Core i3 hoặc tương đương trở lên |
| Container | Docker, Docker Compose (Judge0) |

---

*Người hỗ trợ: OpenCode AI*
*File documentation: PROJECT_ANALYSIS.md*