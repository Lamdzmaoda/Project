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

*Người hỗ trợ: OpenCode AI*
*File documentation: PROJECT_ANALYSIS.md*