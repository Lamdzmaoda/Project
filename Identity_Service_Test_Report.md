# Identity Service — Test Case Report

> **Generated**: May 24, 2026  
> **Total Test Classes**: 29  
> **Total Test Methods**: 184  
> **Pass**: 183 | **Error**: 1 | **Pass Rate**: 99.5%

---

## Table of Contents

1. [Executive Summary](#executive-summary)
2. [Controller Tests](#controller-tests)
   - [AuthenticationControllerTest](#authenticationcontrollertest)
   - [PermissionControllerTest](#permissioncontrollertest)
   - [RoleControllerTest](#rolecontrollertest)
   - [UserControllerTest](#usercontrollertest)
   - [UserControllerIntegrationTest](#usercontrollerintegrationtest)
   - [ChatControllerTest](#chatcontrollertest)
   - [CommunityControllerTest](#communitycontrollertest)
   - [AdminCommunityControllerTest](#admincommunitycontrollertest)
   - [CourseControllerTest](#coursecontrollertest)
   - [EnrollmentControllerTest](#enrollmentcontrollertest)
   - [PistonAPIControllerTest](#pistonapicontrollertest)
   - [UserLearningControllerTest](#userlearningcontrollertest)
3. [Service Tests](#service-tests)
   - [AuthenticationServiceTest](#authenticationservicetest)
   - [ChatServiceTest](#chatservicetest)
   - [CloudinaryServiceTest](#cloudinaryservicetest)
   - [CommentServiceTest](#commentservicetest)
   - [CourseServiceTest](#courseservicetest)
   - [EnrollmentServiceTest](#enrollmentservicetest)
   - [FollowServiceTest](#followservicetest)
   - [Judge0APIServiceTest](#judge0apiservicetest)
   - [LearningProgressServiceTest](#learningprogressservicetest)
   - [LearningServiceTest](#learningservicetest)
   - [PermissionServiceTest](#permissionservicetest)
   - [PostLikeServiceTest](#postlikeservicetest)
   - [PostServiceTest](#postservicetest)
   - [PracticeServiceTest](#practiceservicetest)
   - [RoleServiceTest](#roleservicetest)
   - [SavedPostServiceTest](#savedpostservicetest)
   - [UserServiceTest](#userservicetest)
4. [Coverage Analysis](#coverage-analysis)
5. [Missing Coverage](#missing-coverage)

---

## Executive Summary

| Metric | Value |
|--------|-------|
| **Total Test Methods** | 184 |
| **Passed** | 183 |
| **Failed / Error** | 1 |
| **Pass Rate** | 99.5% |
| **Test Classes** | 29 |
| **Controller Coverage** | 100% (11/11 active) |
| **Service Coverage** | 94% (17/18) |

### Results by Module

| Module | Methods | Pass | Error |
|--------|--------:|-----:|------:|
| Controller - Auth | 26 | 26 | 0 |
| Controller - Community | 32 | 32 | 0 |
| Controller - Learning | 28 | 28 | 0 |
| Controller - Other | 3 | 3 | 0 |
| Service - AI | 2 | 2 | 0 |
| Service - Auth | 29 | 29 | 0 |
| Service - Cloudinary | 2 | 2 | 0 |
| Service - Community | 20 | 20 | 0 |
| Service - Learning | 29 | 29 | 0 |
| Service - System | 1 | 1 | 0 |
| **Integration** | 1 | 0 | 1 |
| **TOTAL** | **184** | **183** | **1** |

---

## Controller Tests

---

### AuthenticationControllerTest

**Package**: `controller/auth`  
**Total**: 9 tests (9 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `authenticate_success` | Success | POST /auth/token | Valid login credentials return JWT token | `200` — token returned, authenticated=true |
| 2 | `introspect_success` | Success | POST /auth/introspect | Valid token introspection | `200` — valid=true, code=2000 |
| 3 | `refreshToken_success` | Success | POST /auth/refresh | Refresh valid token to get new one | `200` — new token returned |
| 4 | `logout_success` | Success | POST /auth/logout | Logout with valid token | `200` |
| 5 | `authenticate_userNotFound_fail` | Failure | POST /auth/token | User not found by username/email | `404` — code=1005 (USER_NOT_EXISTED) |
| 6 | `authenticate_wrongPassword_fail` | Failure | POST /auth/token | Password does not match | `401` — code=1006 (UNAUTHENTICATED) |
| 7 | `introspect_invalidToken_fail` | Failure | POST /auth/introspect | Tampered/invalid JWT token | `200` — valid=false |
| 8 | `refreshToken_expired_fail` | Failure | POST /auth/refresh | Token expired / unauthenticated | `403` — code=1007 (UNAUTHORIZED) |
| 9 | `outboundAuthenticate_success` | Success | POST /auth/outbound/authentication | Google OAuth2 authentication with auth code | `200` — token returned |

---

### PermissionControllerTest

**Package**: `controller/auth`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `create_success` | Success | POST /permissions | Admin creates a new permission | `200` — result.name = "READ_USER" |
| 2 | `getAll_success` | Success | GET /permissions | Admin lists all permissions | `200` — result[0].name = "READ_USER" |
| 3 | `delete_success` | Success | DELETE /permissions/{perm} | Admin deletes a permission | `200` |

---

### RoleControllerTest

**Package**: `controller/auth`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `create_success` | Success | POST /roles | Admin creates role with permissions | `200` — result.name = "MODERATOR" |
| 2 | `getAll_success` | Success | GET /roles | Admin lists all roles | `200` — result[0].name = "MODERATOR" |
| 3 | `delete_success` | Success | DELETE /roles/{role} | Admin deletes a role | `200` |

---

### UserControllerTest

**Package**: `controller/auth`  
**Total**: 13 tests (13 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `CreateUser_validRequest_success` | Success | POST /users | Create user with valid request | `200` — code=1000, result.id returned |
| 2 | `CreateUser_usernameInvalid_fail` | Failure | POST /users | Username too short (3 chars, min 4) | `400` — code=1002, validation error |
| 3 | `CreateUser_passwordInvalid_fail` | Failure | POST /users | Password too short (5 chars, min 8) | `400` — code=1003, validation error |
| 4 | `CreateUser_userExisted_fail` | Failure | POST /users | Username already exists | `400` — code=1001 (USER_EXISTED) |
| 5 | `GetUsers_validRequest_success` | Success | GET /users | Admin lists all users | `200` — result[0].username returned |
| 6 | `GetMyInfo_validRequest_success` | Success | GET /users/myInfo | Get own profile info | `200` — result.username returned |
| 7 | `GetUserById_validRequest_success` | Success | GET /users/{id} | Admin gets user by ID | `200` — result.username returned |
| 8 | `GetUserById_userNotFound_fail` | Failure | GET /users/{id} | User ID not found | `404` — code=1005 (USER_NOT_EXISTED) |
| 9 | `UpdateUser_validRequest_success` | Success | PUT /users/{id} | Admin updates a user | `200` — username & displayName returned |
| 10 | `GetMyInfo_userNotFound_fail` | Failure | GET /users/myInfo | Authenticated user not in DB | `404` — code=1005 (USER_NOT_EXISTED) |
| 11 | `UpdateUser_userNotFound_fail` | Failure | PUT /users/{id} | User not found for update | `404` — code=1005 |
| 12 | `UpdateUser_notOwner_fail` | Failure | PUT /users/{id} | Unauthorized update attempt | `403` — code=1007 (UNAUTHORIZED) |
| 13 | `DeleteUser_success` | Success | DELETE /users/{id} | Admin deletes a user | `200` — body = "success" |

---

### UserControllerIntegrationTest

**Package**: `controller`  
**Total**: 1 test (1 Error)

| # | Method | Type | HTTP | Description | Expected Result | Actual |
|---|--------|------|------|-------------|-----------------|--------|
| 1 | `CreateUser_validRequest_success` | Integration | POST /users | End-to-end test with MySQL TestContainers | `200` — user created in real DB | **Error** — requires Docker/TestContainers |

> **Note**: This test requires a running Docker environment for TestContainers MySQL container. Not executable in environments without Docker.

---

### ChatControllerTest

**Package**: `controller`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `chat_success` | Success | POST /chats | Send message and get AI response | `200` — userMessage + aiExplanation returned |
| 2 | `getChat_success` | Success | GET /chats | Get all chat histories | `200` — list of chat responses |
| 3 | `getChatOfUser_success` | Success | GET /chats/user | Get current user's chat history | `200` — list of user's chats |

---

### CommunityControllerTest

**Package**: `controller/community`  
**Total**: 28 tests (28 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `createPost_success` | Success | POST /api/community/posts | Create a new post | `200` — result.id + result.title returned |
| 2 | `getFeed_success` | Success | GET /api/community/posts | Get paginated feed | `200` — result.content[0].id returned |
| 3 | `getPost_success` | Success | GET /api/community/posts/{postId} | Get single post by ID | `200` — result.id returned |
| 4 | `getPost_notFound_fail` | Failure | GET /api/community/posts/{postId} | Post ID not found | `400` — code=1009 (ID_NOT_EXISTED) |
| 5 | `getUserPosts_success` | Success | GET /api/community/users/{userId}/posts | Get paginated user posts | `200` — result.content[0].id returned |
| 6 | `deletePost_success` | Success | DELETE /api/community/posts/{postId} | Delete own post | `200` |
| 7 | `deletePost_notOwner_fail` | Failure | DELETE /api/community/posts/{postId} | Non-owner deletes post | `403` — code=1007 (UNAUTHORIZED) |
| 8 | `createComment_success` | Success | POST /api/community/comments | Create comment on post | `200` — result.id + result.content returned |
| 9 | `getComments_success` | Success | GET /api/community/posts/{postId}/comments | Get comments for post | `200` — list of comments |
| 10 | `deleteComment_success` | Success | DELETE /api/community/comments/{commentId} | Delete own comment | `200` |
| 11 | `likePost_success` | Success | POST /api/community/posts/{postId}/like | Like a post | `200` — result.id returned |
| 12 | `unlikePost_success` | Success | DELETE /api/community/posts/{postId}/like | Unlike a post | `200` |
| 13 | `isLiked_true` | Success | GET /api/community/posts/{postId}/liked | Check if liked (true) | `200` — result = true |
| 14 | `isLiked_false` | Success | GET /api/community/posts/{postId}/liked | Check if liked (false) | `200` — result = false |
| 15 | `getFollowingFeed_success` | Success | GET /api/community/feed/following | Get following feed | `200` — result.content[0].id returned |
| 16 | `follow_success` | Success | POST /api/community/follow/{followeeId} | Follow a user | `200` — result.followeeId returned |
| 17 | `unfollow_success` | Success | DELETE /api/community/follow/{followeeId} | Unfollow a user | `200` |
| 18 | `getFollowing_success` | Success | GET /api/community/users/{userId}/following | Get following list | `200` — list of followees |
| 19 | `getFollowers_success` | Success | GET /api/community/users/{userId}/followers | Get followers list | `200` — list of followers |
| 20 | `getFollowingCount_success` | Success | GET /api/community/users/{userId}/following/count | Get following count | `200` — result = 5 |
| 21 | `getFollowerCount_success` | Success | GET /api/community/users/{userId}/followers/count | Get follower count | `200` — result = 10 |
| 22 | `isFollowing_success` | Success | GET /api/community/follow/{followeeId}/check | Check if following | `200` — result = true |
| 23 | `savePost_success` | Success | POST /api/community/posts/{postId}/save | Save a post | `200` — result.postId returned |
| 24 | `unsavePost_success` | Success | DELETE /api/community/posts/{postId}/save | Unsave a post | `200` |
| 25 | `getSavedPosts_success` | Success | GET /api/community/saved-posts | Get all saved posts | `200` — list of saved posts |
| 26 | `isSaved_true` | Success | GET /api/community/posts/{postId}/saved | Check if saved (true) | `200` — result = true |
| 27 | `isSaved_false` | Success | GET /api/community/posts/{postId}/saved | Check if saved (false) | `200` — result = false |
| 28 | `uploadImage_success` | Success | POST /api/community/upload-image | Upload image to Cloudinary | `200` — image URL returned |

---

### AdminCommunityControllerTest

**Package**: `controller/community`  
**Total**: 4 tests (4 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `adminDeletePost_success` | Success | DELETE /admin/community/posts/{postId} | Admin deletes any post | `200` |
| 2 | `adminDeleteComment_success` | Success | DELETE /admin/community/comments/{commentId} | Admin deletes any comment | `200` |
| 3 | `adminDeletePost_forbidden_whenNotAdmin` | Auth Failure | DELETE /admin/community/posts/{postId} | Non-admin tries to delete post | `403` Forbidden |
| 4 | `adminDeleteComment_forbidden_whenNotAdmin` | Auth Failure | DELETE /admin/community/comments/{commentId} | Non-admin tries to delete comment | `403` Forbidden |

---

### CourseControllerTest

**Package**: `controller/learning`  
**Total**: 15 tests (15 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `createStep_success` | Success | POST /course/steps | Admin creates a step | `200` — result.id = "step-1" |
| 2 | `createProblem_success` | Success | POST /course/problems | Admin creates a problem | `200` — result.id = "problem-1" |
| 3 | `createLesson_success` | Success | POST /course/lessons | Admin creates a lesson | `200` — result.id = "lesson-1" |
| 4 | `createChapter_success` | Success | POST /course/chapters | Admin creates a chapter | `200` — result.id = "chapter-1" |
| 5 | `createLanguage_success` | Success | POST /course/languages | Admin creates a language | `200` — result.name = "java" |
| 6 | `getStep_success` | Success | GET /course/steps/{stepId} | Get step by ID | `200` — result.id = "step-1" |
| 7 | `getProblem_success` | Success | GET /course/problems/{problemId} | Get problem by ID | `200` — result.id = "problem-1" |
| 8 | `getLesson_success` | Success | GET /course/lessons/{lessonId} | Get lesson by ID | `200` — result.id = "lesson-1" |
| 9 | `getChapter_success` | Success | GET /course/chapters/{chapterId} | Get chapter by ID | `200` — result.id = "chapter-1" |
| 10 | `getLanguage_success` | Success | GET /course/languages/{languageName} | Get language by name | `200` — result.name = "java" |
| 11 | `getStepsByLessonForAdmin_success` | Success | GET /course/steps/admin/{lessonId} | Admin gets all steps (incl. inactive) | `200` — result[0].id = "step-1" |
| 12 | `getLanguages_success` | Success | GET /course/languages | Admin lists all languages | `200` — result[0].name = "java" |
| 13 | `updateStep_success` | Success | PUT /course/steps/{stepId} | Admin updates a step | `200` — result.title = "Updated Step" |
| 14 | `deleteStep_success` | Success | DELETE /course/steps/{stepId} | Admin soft-deletes a step | `200` |
| 15 | `purgeStep_success` | Success | DELETE /course/steps/admin/{stepId} | Admin hard-deletes a step | `200` |

---

### EnrollmentControllerTest

**Package**: `controller/learning`  
**Total**: 2 tests (2 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `enrollCourse_success` | Success | POST /enrollment | Enroll in a language course | `200` — result.id + result.userName returned |
| 2 | `enrollCourse_languageNotFound_fail` | Failure | POST /enrollment | Language not found | `400` — code=1009 (ID_NOT_EXISTED) |

---

### PistonAPIControllerTest

**Package**: `controller/learning`  
**Total**: 1 test (1 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `runTest_success` | Success | POST /api/v1/code/run | Execute Python code via Piston API | `200` — result.output returned, result.passed = true |

---

### UserLearningControllerTest

**Package**: `controller/learning`  
**Total**: 10 tests (10 Pass)

| # | Method | Type | HTTP | Description | Expected Result |
|---|--------|------|------|-------------|-----------------|
| 1 | `getStepByLessonId_success` | Success | GET /my-learning/lessons/{lessonId}/steps | Get steps for a lesson | `200` — result[0].id = "step-1" |
| 2 | `getProblem_success` | Success | GET /my-learning/problems/{lessonId}/problems | Get problems for a lesson | `200` — result[0].id = "problem-1" |
| 3 | `getLessonByChapterId_success` | Success | GET /my-learning/chapters/{chapterId}/lessons | Get lessons for a chapter | `200` — result[0].id = "lesson-1" |
| 4 | `getChapterByLanguageName_success` | Success | GET /my-learning/{languageName}/chapters | Get chapters for a language | `200` — result[0].id = "chapter-1" |
| 5 | `getLearningProgress_success` | Success | GET /my-learning/{language}/progress | Get learning progress stats | `200` — result.currentXp = 50 |
| 6 | `verifyLesson_success` | Success | POST /my-learning/verify | Verify lesson answers | `200` — result.totalXpGained = 100 |
| 7 | `getMyLearning_success` | Success | GET /my-learning/{languageName} | Get full learning structure + progress | `200` — result[0].id = "chapter-1" |
| 8 | `submitPractice_success` | Success | POST /my-learning/practice/submit | Submit code for a problem | `200` — result.passed = true |
| 9 | `completePractice_success` | Success | POST /my-learning/practice/complete | Complete a practice lesson | `200` — result.passed = true, result.xp = 100 |
| 10 | `resetProgress_success` | Success | DELETE /my-learning | Reset learning progress for a language | `200` |

---

## Service Tests

---

### AuthenticationServiceTest

**Package**: `service`  
**Total**: 8 tests (8 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `authenticate_userNotFound_fail` | Failure | User not found by username or email during login | `userRepository.findByUsername` → empty, `userRepository.findByEmail` → empty | AppException(1005) |
| 2 | `authenticate_passwordWrong_fail` | Failure | User found but wrong password | `userRepository.findByUsername` → user, `passwordEncoder.matches` → false | AppException(1006) |
| 3 | `authenticate_validRequest_success` | Success | Valid login → JWT token generated | `userRepository.findByUsername` → user, `passwordEncoder.matches` → true | authenticated=true, token != null |
| 4 | `introspect_validToken_success` | Success | Valid JWT introspection | `invalidatedTokenRepository.existsById` → false | valid=true |
| 5 | `introspect_invalidToken_fail` | Failure | Tampered JWT (signature fails) | No mocks needed | valid=false |
| 6 | `logout_success` | Success | Logout invalidates valid token | No mocks needed | assertDoesNotThrow |
| 7 | `refresh_token_success` | Success | Refresh valid token → new token | `userRepository.findByUsername` → user | authenticated=true, token != null |
| 8 | `outboundAuthenticate_success` | Success | Google OAuth2 with auth code | `outboundIdentityClient.exchangeToken` → exchangeTokenResponse | token = "google-access-token" |

---

### ChatServiceTest

**Package**: `service`  
**Total**: 2 tests (2 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `getHistorys_success` | Success | Get all chat histories | `chatRepository.findAll` → list of entities, `chatMapper.toChatResponse` → response | size=1, userMessage="hello" |
| 2 | `getHistoryByUser_success` | Success | Get chat history for authenticated user | `userRepository.findByUsername` → user, `chatRepository.findByUser` → list, `chatMapper.toChatResponse` → response | size=1, userMessage="hello" |

---

### CloudinaryServiceTest

**Package**: `service`  
**Total**: 2 tests (2 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `uploadFile_success` | Success | Upload image to Cloudinary | `cloudinary.uploader()` → uploader, `uploader.upload` → Map with secure_url | result = "https://res.cloudinary.com/test.jpg" |
| 2 | `uploadFile_ioException_fail` | Failure | Cloudinary upload throws IOException | `cloudinary.uploader()` → uploader, `uploader.upload` → throws IOException | AppException(1026) |

---

### CommentServiceTest

**Package**: `service`  
**Total**: 7 tests (7 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `createComment_success` | Success | Create comment on a post | `userRepository.findById` → user, `postRepository.findById` → post, `commentMapper.toComment` → comment, `commentRepository.save` → comment | result != null |
| 2 | `createComment_postNotFound_fail` | Failure | Post not found | `userRepository.findById` → user, `postRepository.findById` → empty | AppException |
| 3 | `createComment_userNotFound_fail` | Failure | User not found | `userRepository.findById` → empty | AppException |
| 4 | `getCommentsByPost_success` | Success | Get comments for a post | `commentRepository.findByPostIdOrderByCreatedAtDesc` → list, `commentMapper.toCommentResponse` → response | result not empty |
| 5 | `deleteComment_success` | Success | Delete own comment | `commentRepository.findById` → comment (owned) | No exception |
| 6 | `deleteComment_notOwner_fail` | Failure | Non-owner deletes comment | `commentRepository.findById` → comment (other user's) | AppException |
| 7 | `deleteComment_notFound_fail` | Failure | Comment not found for deletion | `commentRepository.findById` → empty | AppException |

---

### CourseServiceTest

**Package**: `service`  
**Total**: 13 tests (13 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `getStepById_success` | Success | Get active step by ID | `stepRepository.findByIdAndStatus` → step, `courseMapper.toStepResponse` → response | result.id = "step-1" |
| 2 | `getStepById_notFound_fail` | Failure | Step not found | `stepRepository.findByIdAndStatus` → empty | AppException |
| 3 | `getProblemById_success` | Success | Get active problem by ID | `problemRepository.findByIdAndStatus` → problem, `courseMapper.toProblemResponse` → response | result.id = "prob-1" |
| 4 | `getProblemById_notFound_fail` | Failure | Problem not found | `problemRepository.findByIdAndStatus` → empty | AppException |
| 5 | `getLessonById_success` | Success | Get active lesson by ID | `lessonRepository.findByIdAndStatus` → lesson, `courseMapper.toLessonResponse` → response | result.id = "lesson-1" |
| 6 | `getChapterById_success` | Success | Get active chapter by ID | `chapterRepository.findByIdAndStatus` → chapter, `courseMapper.toChapterResponse` → response | result.id = "chap-1" |
| 7 | `getLanguageById_success` | Success | Get active language by name | `languageRepository.findByNameAndStatus` → language, `courseMapper.toLanguageResponse` → response | result.name = "java" |
| 8 | `getStepByLesson_success` | Success | Get steps for a lesson ordered by index | `stepRepository.findAllByLessonIdAndStatusOrderByOrderIndexAsc` → list, `courseMapper.toStepResponse` → response | size=1, result[0].id = "step-1" |
| 9 | `getProblemByLesson_success` | Success | Get problems for a lesson ordered by index | `problemRepository.findAllByLessonIdAndStatusOrderByOrderIndexAsc` → list, `courseMapper.toProblemResponse` → response | size=1, result[0].id = "prob-1" |
| 10 | `getLessonByChapter_success` | Success | Get lessons for a chapter ordered by index | `lessonRepository.findAllByChapterIdAndStatusOrderByOrderIndexAsc` → list, `courseMapper.toLessonResponse` → response | size=1, result[0].id = "lesson-1" |
| 11 | `getChapterByLanguage_success` | Success | Get chapters for a language ordered by index | `chapterRepository.findAllByLanguageNameAndStatusOrderByOrderIndexAsc` → list, `courseMapper.toChapterResponse` → response | size=1, result[0].id = "chap-1" |
| 12 | `getMyLearning_success` | Success | Get learning view with locked/unlocked status | `learningProgressService.getCurrentUser` → user, multiple chapter + lesson queries, `courseMapper.toStepResponse` → response | result[0].id = "chap-1", lockedStatus = TRUE_LOCKED |
| 13 | `getLearningProgress_success` | Success | Get aggregated progress stats (XP, counts) | `learningProgressService.getCurrentUser` → user, `enrollmentRepository.findByUserAndLanguage` → Enrollment(currentXp=50) | currentXp = 50, completedLessons = 2, totalLessons = 5 |

---

### EnrollmentServiceTest

**Package**: `service`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `enrollCourse_success` | Success | Enroll user in a language course | `userRepository.findByUsername` → user, `languageRepository.findById` → language, `enrollmentRepository.findByUserAndLanguage` → empty, `enrollmentRepository.save` → enrollment | result.userName = "testuser" |
| 2 | `enrollCourse_languageNotFound_fail` | Failure | Language not found | `userRepository.findByUsername` → user, `languageRepository.findById` → empty | AppException |
| 3 | `enrollCourse_userNotFound_fail` | Failure | Authenticated user not found in DB | `userRepository.findByUsername` → empty | AppException |

---

### FollowServiceTest

**Package**: `service`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `follow_self_fail` | Failure | User tries to follow themselves | Same follower and followee ID | AppException |
| 2 | `follow_alreadyExists_fail` | Failure | Follow relationship already exists | `followRepository.existsByFollowerIdAndFolloweeId` → true | AppException |
| 3 | `follow_success` | Success | Follow another user successfully | `userRepository.findById` → follower + followee, `followRepository.existsBy...` → false, `followRepository.save` → follow | result != null |

---

### Judge0APIServiceTest

**Package**: `service`  
**Total**: 1 test (1 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `executePythonCode_error_whenNoServer` | Failure | Code execution fails (no server) | `codeMapper.toCode` → codeEntity, `codeMapper.toCodeResponse` → CodeResponse(ERROR) | result.status = Status.ERROR |

---

### LearningProgressServiceTest

**Package**: `service`  
**Total**: 6 tests (6 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `getCurrentUser_success` | Success | Get authenticated user from DB | `userRepository.findByUsername` → user | result.username = "testuser" |
| 2 | `getCurrentUser_unauthenticated_fail` | Failure | No authenticated user | SecurityContext cleared | AppException |
| 3 | `completeLesson_updatesXpAndStreak` | Success | Complete lesson → XP=50, streak=1, lastActivity=today | Multiple progress queries, `enrollmentRepository.findByUserAndLanguage` → Enrollment | user.totalXp=50, user.streak=1, user.lastActivityDate=today |
| 4 | `completeLesson_alreadyCompleted_skips` | Edge | Lesson already completed → no re-processing | `userLessonProgressRepository.findByUserAndLesson` → existing with completed=TRUE | completedStatus remains TRUE |
| 5 | `initializeLearningProgressForLanguage_success` | Success | Initialize progress for new language enrollment | `chapterRepository.findFirstByLanguageAndStatusOrderByOrderIndexAsc` → chapter, `userChapterProgressRepository.findByUserAndChapter` → empty | save called for chapter + lesson progress |
| 6 | `restProgress_success` | Success | Reset all progress for a language | `userRepository.findByUsername` → user, `languageRepository.findByNameAndStatus` → language, `enrollmentRepository.findByUserAndLanguage` → Enrollment(100XP, 50%) | deletes submissions, chapter & lesson progress; resets XP to 0 |

---

### LearningServiceTest

**Package**: `service`  
**Total**: 7 tests (7 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `verifyLesson_success` | Success | All answers correct → 100 XP gained | `learningProgressService.getCurrentUser` → user, `lessonRepository.findByIdAndStatus` → lesson (2 steps) | totalXpGained=100, isNewlyCompleted=true, verifyResponses[0].isCorrect=true |
| 2 | `verifyLesson_lessonNotFound_fail` | Failure | Lesson ID not found | `lessonRepository.findByIdAndStatus` → empty | AppException |
| 3 | `verifyLesson_stepMismatch_fail` | Failure | Step count mismatch (1 submitted vs 2 expected) | `lessonRepository.findByIdAndStatus` → lesson (2 steps) | AppException |
| 4 | `verifyLesson_duplicateStepId_fail` | Failure | Duplicate step IDs in request | `lessonRepository.findByIdAndStatus` → lesson (2 steps) | AppException |
| 5 | `verifyLesson_wrongAnswer_fail` | Failure | One answer is wrong | `learningProgressService.getCurrentUser` → user, `lessonRepository.findByIdAndStatus` → lesson, `userLessonProgressRepository.findByUserAndLesson` → empty | AppException |
| 6 | `verifyLesson_lockedLesson_fail` | Failure | Lesson is locked | `learningProgressService.getCurrentUser` → user, `lessonRepository.findByIdAndStatus` → lesson, `userLessonProgressRepository.findByUserAndLesson` → progress(locked=TRUE_LOCKED) | AppException |
| 7 | `verifyLesson_alreadyCompleted_returnsZeroXp` | Edge | Lesson already completed → re-verify yields 0 XP | `learningProgressService.getCurrentUser` → user, `userLessonProgressRepository.findByUserAndLesson` → progress(completed=TRUE) | totalXpGained=0, isNewlyCompleted=false |

---

### PermissionServiceTest

**Package**: `service`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `create_success` | Success | Create a new permission | `permissionMapper.toPermission` → permission, `permissionRepository.save` → permission, `permissionMapper.toPermissionResponse` → response | result.name = "READ_USER" |
| 2 | `getAll_success` | Success | Get all permissions | `permissionRepository.findAll` → list, `permissionMapper.toPermissionResponse` → response | size=1, result[0].name = "READ_USER" |
| 3 | `delete_success` | Success | Delete a permission | No mocks needed | assertDoesNotThrow |

---

### PostLikeServiceTest

**Package**: `service`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `likePost_success` | Success | Like a post not yet liked | `userRepository.findById` → user, `postRepository.findById` → post, `postLikeRepository.findByPostIdAndUserId` → empty, `postLikeRepository.save` → PostLike | result != null |
| 2 | `likePost_alreadyLiked_fail` | Failure | Post already liked | `userRepository.findById` → user, `postRepository.findById` → post, `postLikeRepository.findByPostIdAndUserId` → existing PostLike | AppException |
| 3 | `unlikePost_notLiked_fail` | Failure | Unlike post never liked | `postLikeRepository.findByPostIdAndUserId` → empty | AppException |

---

### PostServiceTest

**Package**: `service`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `createPost_success` | Success | Create a new post | `userRepository.findById` → user, `postMapper.toPost` → post, `postRepository.save` → post, `postMapper.toPostResponse` → response | result != null |
| 2 | `getPostById_notFound_fail` | Failure | Get post by non-existent ID | `postRepository.findById` → empty | AppException |
| 3 | `deletePost_notOwner_fail` | Failure | Non-owner deletes post | `postRepository.findById` → post (owned by other user) | AppException |

---

### PracticeServiceTest

**Package**: `service`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `summit_success` | Success | Submit code → output matches expected | `problemRepository.findById` → problem, `judge0APIService.executePythonCode` → CodeResponse(output="5", SUCCESS), `learningProgressService.getCurrentUser` → user | result.passed = true |
| 2 | `summit_problemNotFound_fail` | Failure | Problem ID not found | `problemRepository.findById` → empty | AppException |
| 3 | `complete_notAllPassed_fail` | Failure | Complete lesson with no passing submissions | `lessonRepository.findById` → lesson, `submissionRepository.findFirstByUserAndProblemOrderByCreatedAtDesc` → empty | result.passed = false |

---

### RoleServiceTest

**Package**: `service`  
**Total**: 3 tests (3 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `create_success` | Success | Create role with permissions | `roleMapper.toRole` → role, `permissionRepository.findAllById` → list, `roleRepository.save` → role, `roleMapper.toRoleResponse` → response | result.name = "MODERATOR" |
| 2 | `getAll_success` | Success | Get all roles | `roleRepository.findAll` → list, `roleMapper.toRoleResponse` → response | size=1, result[0].name = "MODERATOR" |
| 3 | `delete_success` | Success | Delete a role by name | No mocks needed | assertDoesNotThrow |

---

### SavedPostServiceTest

**Package**: `service`  
**Total**: 7 tests (7 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `savePost_success` | Success | Save a post for later | `savedPostRepository.existsByUserIdAndPostId` → false, `userRepository.findById` → user, `postRepository.findById` → post, `savedPostRepository.save` → savedPost | result.postId = "post-1" |
| 2 | `savePost_alreadyExists_fail` | Failure | Post already saved by user | `savedPostRepository.existsByUserIdAndPostId` → true | AppException |
| 3 | `unsavePost_success` | Success | Remove a saved post | `savedPostRepository.findByUserIdAndPostId` → savedPost | No exception |
| 4 | `unsavePost_notFound_fail` | Failure | Saved post not found for unsave | `savedPostRepository.findByUserIdAndPostId` → empty | AppException |
| 5 | `getSavedPosts_success` | Success | Get all saved posts for user | `savedPostRepository.findByUserIdOrderBySavedAtDesc` → list, `postService.getPostById` → postResponse | result not empty |
| 6 | `isSaved_true` | Success | Check if post is saved (true) | `savedPostRepository.existsByUserIdAndPostId` → true | result = true |
| 7 | `isSaved_false` | Success | Check if post is saved (false) | `savedPostRepository.existsByUserIdAndPostId` → false | result = false |

---

### UserServiceTest

**Package**: `service`  
**Total**: 18 tests (18 Pass)

| # | Method | Type | Description | Key Mocks | Expected Result |
|---|--------|------|-------------|-----------|-----------------|
| 1 | `CreateUser_validRequest_success` | Success | Create user with valid request | `userRepository.existsByUsername` → false, `userMapper.toUser` → user, `passwordEncoder.encode` → "encodedPassword", `roleRepository.findById` → role | response.username = "lamdzbodoi", response.email returned |
| 2 | `CreateUser_userExisted_fail` | Failure | Username already exists | `userRepository.existsByUsername` → true | AppException(1001) |
| 3 | `CreateUser_emailExisted_fail` | Failure | Email already exists | `userRepository.existsByEmail` → true | AppException(1025) |
| 4 | `RoleNotFound_fail` | Failure | Default role (USER) not found | `userRepository.existsByUsername` → false, `userRepository.existsByEmail` → false, `roleRepository.findById` → empty | AppException(9999) |
| 5 | `CreateUser_duplicateRaceConditions_fail` | Edge | Race condition: save throws DataIntegrityViolationException | pre-checks pass, `userRepository.save` → throws DataIntegrityViolationException | AppException(1001) |
| 6 | `UpdateUser_validRequest_fail` | Failure | Admin updates non-existent user | `userRepository.findById` → empty | AppException(1005) |
| 7 | `UpdateUser_validRequest_success` | Success | Admin updates any user | `userRepository.findById` → user, `roleRepository.findAllById` → list, `userRepository.save` → user | response.username = "lamdzbodoi" |
| 8 | `UpdateUser_owner_success` | Success | Owner updates own profile | `userRepository.findById` → user (same user as auth) | response.username = "lamdzbodoi" |
| 9 | `UpdateUser_notOwner_fail` | Failure | Non-owner tries to update profile | `userRepository.findById` → user (different user from auth) | AuthorizationDeniedException |
| 10 | `updatePassword_wrongPassword_fail` | Failure | Current password does not match stored | `userRepository.findByUsername` → user | AppException(1006) |
| 11 | `updatePassword_success` | Success | Password updated with correct current password | `userRepository.findByUsername` → user, `passwordEncoder.encode` → "lamdzbodoi" | response.username = "lamdzbodoi" |
| 12 | `getMyInfo_valid_success` | Success | Get own profile info | `userRepository.findByUsername` → user, `userMapper.toUserResponse` → response | response.username = "lamdzbodoi" |
| 13 | `getMyInfo_userNotFound_error` | Failure | Authenticated user not found in DB | `userRepository.findByUsername` → empty | AppException(1005) |
| 14 | `getUser_validRequest_success` | Success | Admin lists all users | `userRepository.findAll` → list, `userMapper.toUserResponse` → response | size=1, result[0].username = "lamdzbodoi" |
| 15 | `getUserById_validRequest_success` | Success | Admin gets user by ID | `userRepository.findById` → user, `userMapper.toUserResponse` → response | response.username = "lamdzbodoi" |
| 16 | `getUserById_userNotFound_fail` | Failure | Admin gets user by non-existent ID | `userRepository.findById` → empty | AppException(1005) |
| 17 | `deleteUserById_success` | Success | Admin deletes user by ID | No mocks needed | assertDoesNotThrow |
| 18 | `updateAvatar_success` | Success | User updates avatar URL | `userRepository.findByUsername` → user | result = "https://example.com/avatar.jpg" |

---

## Coverage Analysis

### By Layer

| Layer | Total Classes | Tested | Missing | Coverage |
|-------|:------------:|:------:|:-------:|:--------:|
| **Controllers (@RestController)** | 11 | 11 | 0 | **100%** |
| **Services (@Service)** | 18 | 17 | 1 | **94%** |
| Components (@Component) | 1 | 0 | 1 | 0% |
| ControllerAdvice | 1 | 0 | 1 | 0% |
| Repositories (@Repository) | 20 | 0 | 20 | 0% |
| Configurations (@Configuration) | 6 | 0 | 6 | 0% |

### By Test Scenario Type

| Scenario Type | Count |
|--------------|:-----:|
| **Success** | ~124 |
| **Failure (not found, validation, auth)** | ~53 |
| **Edge cases** | ~4 |
| **Authorization/Security** | ~3 |

---

## Missing Coverage

Items within the **Service + Controller** scope that are not yet tested:

| # | Class | Type | Package | Reason |
|---|-------|------|---------|--------|
| 1 | `ChatServiceTest` (main source) | @Service | service.ai | Depends on Spring AI ChatClient; testing requires complex mock setup; deferred |
| 2 | `UserSecurity` | @Component | service.auth | Out of scope (not service/controller per user request) |
| 3 | `GlobalExceptionHandler` | @ControllerAdvice | exception | Out of scope (not service/controller per user request) |
| 4 | `GradingController` | (commented out) | controller | Entire file is commented out (`// @RestController`); not active |

---

*End of Report — 184 Test Cases Documented*
