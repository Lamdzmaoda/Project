from docx import Document
from docx.shared import Inches, Pt, Cm, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.section import WD_ORIENT
from docx.oxml.ns import qn
from docx.oxml import OxmlElement
import os

doc = Document()

# ─── Styles ───
style = doc.styles['Normal']
font = style.font
font.name = 'Times New Roman'
font.size = Pt(12)
style.paragraph_format.line_spacing = 1.5

# ─── Helper Functions ───
def set_cell_shading(cell, color):
    shading = OxmlElement('w:shd')
    shading.set(qn('w:fill'), color)
    shading.set(qn('w:val'), 'clear')
    cell._tc.get_or_add_tcPr().append(shading)

def add_table_with_header(doc, headers, rows, col_widths=None):
    table = doc.add_table(rows=1, cols=len(headers))
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.style = 'Table Grid'
    
    # Header
    for i, h in enumerate(headers):
        cell = table.rows[0].cells[i]
        cell.text = h
        for p in cell.paragraphs:
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            for r in p.runs:
                r.bold = True
                r.font.size = Pt(10)
                r.font.color.rgb = RGBColor(255, 255, 255)
        set_cell_shading(cell, '2F5496')
    
    # Data rows
    for row_data in rows:
        row = table.add_row()
        for i, val in enumerate(row_data):
            cell = row.cells[i]
            cell.text = str(val)
            for p in cell.paragraphs:
                for r in p.runs:
                    r.font.size = Pt(10)
            if col_widths and i < len(col_widths):
                cell.width = Cm(col_widths[i])
    
    return table

# ─── Page Border Function ───
def add_page_border(doc):
    sect = doc.sections[0]
    sect.left_margin = Cm(2.5)
    sect.right_margin = Cm(2.5)
    sect.top_margin = Cm(2.5)
    sect.bottom_margin = Cm(2.5)

add_page_border(doc)

# ═══════════════════════════════════════════
# COVER PAGE
# ═══════════════════════════════════════════
for _ in range(6):
    doc.add_paragraph()

title = doc.add_paragraph()
title.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = title.add_run('BÁO CÁO TEST COVERAGE')
run.bold = True
run.font.size = Pt(28)
run.font.color.rgb = RGBColor(47, 84, 150)

subtitle = doc.add_paragraph()
subtitle.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = subtitle.add_run('IDENTITY SERVICE')
run.bold = True
run.font.size = Pt(20)
run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_paragraph()

info = doc.add_paragraph()
info.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = info.add_run('Dự án: Identity Service - Spring Boot\n'
                    'Công nghệ: Java 21, Spring Boot 3.x, JUnit 5, Mockito\n'
                    'Database: MySQL (test: H2)\n'
                    'Ngày báo cáo: 23/05/2026')
run.font.size = Pt(12)
run.font.color.rgb = RGBColor(100, 100, 100)

doc.add_page_break()

# ═══════════════════════════════════════════
# TABLE OF CONTENTS (Manual)
# ═══════════════════════════════════════════
toc_title = doc.add_paragraph()
toc_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = toc_title.add_run('MỤC LỤC')
run.bold = True
run.font.size = Pt(18)
run.font.color.rgb = RGBColor(47, 84, 150)

toc_items = [
    '1. Tổng quan hệ thống test',
    '2. Kiến trúc test',
    '3. UserService Test',
    '    3.1 createUser()',
    '    3.2 updateUser()',
    '    3.3 getMyInfo()',
    '    3.4 getUser() & getUserById()',
    '    3.5 updatePassword()',
    '    3.6 deleteUserById()',
    '4. AuthenticationService Test',
    '    4.1 authenticate()',
    '    4.2 introspect()',
    '    4.3 logout() & refreshToken()',
    '5. UserController Test',
    '    5.1 POST /users',
    '    5.2 GET /users',
    '    5.3 GET /users/myInfo & /users/{id}',
    '    5.4 PUT /users/{id} & DELETE /users/{id}',
    '6. AuthenticationController Test',
    '7. Thống kê tổng hợp',
    '8. Hướng dẫn chạy test',
    '9. [TODO] Các test cần bổ sung',
]
for item in toc_items:
    p = doc.add_paragraph(item)
    p.paragraph_format.space_after = Pt(2)
    for r in p.runs:
        r.font.size = Pt(12)

doc.add_page_break()

# ═══════════════════════════════════════════
# 1. OVERVIEW
# ═══════════════════════════════════════════
h1 = doc.add_heading('1. Tổng Quan Hệ Thống Test', level=1)
for run in h1.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_paragraph(
    'Hệ thống test được xây dựng cho dự án Identity Service với mục tiêu đảm bảo chất lượng '
    'cho các tầng Service và Controller. Báo cáo này trình bày chi tiết các test case đã triển khai, '
    'kết quả đạt được và các khu vực cần bổ sung trong tương lai.'
)

doc.add_paragraph()

overview_headers = ['Tiêu chí', 'Giá trị']
overview_rows = [
    ['Tổng số test cases', '157'],
    ['Số service tests', '64 (Auth: 26 + Community: 23 + Learning: 9 + System: 2 + Other: 4)'],
    ['Số controller tests', '92 (Auth: 28 + Community: 32 + Learning: 28 + AI: 3 + Other: 1)'],
    ['Số test files', '26 files'],
    ['Framework test', 'JUnit 5 + Mockito + Spring MockMvc'],
    ['Database test', 'H2 In-Memory (MYSQL mode)'],
    ['Coverage mục tiêu', '100% các business case'],
]
add_table_with_header(doc, overview_headers, overview_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 2. KIẾN TRÚC TEST
# ═══════════════════════════════════════════
h2 = doc.add_heading('2. Kiến Trúc Test', level=1)
for run in h2.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_heading('2.1. Service Layer Testing', level=2)
doc.add_paragraph(
    '• Sử dụng @SpringBootTest để load application context\n'
    '• @MockitoBean để mock các dependency (Repository, Mapper, ...)\n'
    '• @WithMockUser để giả lập authentication cho các method có @PreAuthorize\n'
    '• Mockito.when().thenReturn() / thenThrow() để định nghĩa behavior\n'
    '• assertThrows() + assertThat() để kiểm tra exception và response'
)

doc.add_heading('2.2. Controller Layer Testing', level=2)
doc.add_paragraph(
    '• Sử dụng @AutoConfigureMockMvc + MockMvc để gửi HTTP request ảo\n'
    '• ObjectMapper + JavaTimeModule để serialize/deserialize JSON\n'
    '• .with(user().roles()) thay cho @WithMockUser (tránh issue authentication filter)\n'
    '• jsonPath() để assert response JSON\n'
    '• MockitoBean cho Service dependency'
)

doc.add_page_break()

# ═══════════════════════════════════════════
# 3. USERSERVICE TEST
# ═══════════════════════════════════════════
h3 = doc.add_heading('3. UserService Test', level=1)
for run in h3.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_paragraph('Tổng số: 17 tests  |  Tỉ lệ pass: 100%')
doc.add_paragraph('File: src/test/java/com/example/identity_servive/service/UserServiceTest.java')

# 3.1
doc.add_heading('3.1. createUser()', level=2)
create_headers = ['#', 'Test Method', 'Scenario', 'Expected Code']
create_rows = [
    ['1', 'CreateUser_validRequest_success', 'Tạo user với dữ liệu hợp lệ', '1000 (Success)'],
    ['2', 'CreateUser_userExisted_fail', 'Username đã tồn tại trong DB', '1001'],
    ['3', 'CreateUser_emailExisted_fail', 'Email đã tồn tại trong DB', '1025'],
    ['4', 'RoleNotFound_fail', 'Role USER không được seed vào DB', '9999'],
    ['5', 'CreateUser_duplicateRaceConditions_fail', 'Race condition (2 request cùng lúc)', '1001'],
]
add_table_with_header(doc, create_headers, create_rows)

doc.add_paragraph()

# 3.2
doc.add_heading('3.2. updateUser()', level=2)
update_headers = ['#', 'Test Method', 'Authentication', 'Scenario', 'Expected']
update_rows = [
    ['1', 'UpdateUser_userNotFound_fail', 'ADMIN', 'User ID không tồn tại', '1005'],
    ['2', 'UpdateUser_validRequest_success', 'ADMIN', 'ADMIN update thành công', 'Success'],
    ['3', 'UpdateUser_owner_success', 'Chính chủ', 'User tự sửa thông tin', 'Success'],
    ['4', 'UpdateUser_notOwner_fail', 'User khác', 'User thường sửa của người khác', 'Access Denied'],
]
add_table_with_header(doc, update_headers, update_rows)

doc.add_paragraph()

# 3.3
doc.add_heading('3.3. getMyInfo()', level=2)
myinfo_headers = ['#', 'Test Method', 'Scenario', 'Expected']
myinfo_rows = [
    ['1', 'getMyInfo_valid_success', 'Lấy thông tin cá nhân thành công', 'Success'],
    ['2', 'getMyInfo_userNotFound_error', 'User không tồn tại (bị xóa giữa chừng)', '1005'],
]
add_table_with_header(doc, myinfo_headers, myinfo_rows)

doc.add_paragraph()

# 3.4
doc.add_heading('3.4. getUser() & getUserById()', level=2)
get_headers = ['#', 'Test Method', 'Authentication', 'Scenario', 'Expected']
get_rows = [
    ['1', 'getUser_validRequest_success', 'ADMIN', 'Lấy danh sách tất cả user', 'Success'],
    ['2', 'getUserById_validRequest_success', 'ADMIN', 'Tìm user theo ID', 'Success'],
    ['3', 'getUserById_userNotFound_fail', 'ADMIN', 'User ID không tồn tại', '1005'],
]
add_table_with_header(doc, get_headers, get_rows)

doc.add_paragraph()

# 3.5
doc.add_heading('3.5. updatePassword()', level=2)
pw_headers = ['#', 'Test Method', 'Authentication', 'Scenario', 'Expected']
pw_rows = [
    ['1', 'updatePassword_wrongPassword_fail', 'Chính chủ', 'Nhập sai mật khẩu cũ', '1006'],
    ['2', 'updatePassword_success', 'Chính chủ', 'Đổi mật khẩu thành công', 'Success'],
]
add_table_with_header(doc, pw_headers, pw_rows)

doc.add_paragraph()

# 3.6
doc.add_heading('3.6. deleteUserById()', level=2)
del_headers = ['#', 'Test Method', 'Authentication', 'Scenario', 'Expected']
del_rows = [
    ['1', 'deleteUserById_success', 'ADMIN', 'Xóa user thành công', 'assertDoesNotThrow'],
]
add_table_with_header(doc, del_headers, del_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 4. AUTHENTICATIONSERVICE TEST
# ═══════════════════════════════════════════
h4 = doc.add_heading('4. AuthenticationService Test', level=1)
for run in h4.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_paragraph('Tổng số: 7 tests  |  Tỉ lệ pass: 100%')
doc.add_paragraph('File: src/test/java/com/example/identity_servive/service/AuthenticationServiceTest.java')

doc.add_heading('4.1. authenticate()', level=2)
auth_headers = ['#', 'Test Method', 'Scenario', 'Expected']
auth_rows = [
    ['1', 'authenticate_userNotFound_fail', 'Username/email không tồn tại', '1005'],
    ['2', 'authenticate_passwordWrong_fail', 'Sai mật khẩu', '1006'],
    ['3', 'authenticate_validRequest_success', 'Đăng nhập thành công', 'Token != null'],
]
add_table_with_header(doc, auth_headers, auth_rows)
doc.add_paragraph()

doc.add_heading('4.2. introspect()', level=2)
intro_headers = ['#', 'Test Method', 'Scenario', 'Expected']
intro_rows = [
    ['1', 'introspect_validToken_success', 'Token hợp lệ, chưa hết hạn', 'valid=true'],
    ['2', 'introspect_invalidToken_fail', 'Token sai chữ ký / hết hạn / blacklist', 'valid=false'],
]
add_table_with_header(doc, intro_headers, intro_rows)
doc.add_paragraph()

doc.add_heading('4.3. logout() & refreshToken()', level=2)
other_headers = ['#', 'Test Method', 'Method', 'Scenario', 'Expected']
other_rows = [
    ['1', 'logout_success', 'logout()', 'Đăng xuất, thêm token vào blacklist', 'assertDoesNotThrow'],
    ['2', 'refreshToken_success', 'refreshToken()', 'Refresh token còn hạn', 'Token mới != null'],
]
add_table_with_header(doc, other_headers, other_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 5. USERCONTROLLER TEST
# ═══════════════════════════════════════════
h5 = doc.add_heading('5. UserController Test', level=1)
for run in h5.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_paragraph('Tổng số: 8 tests  |  Tỉ lệ pass: 100%')
doc.add_paragraph('File: src/test/java/com/example/identity_servive/controller/UserControllerTest.java')

doc.add_heading('5.1. POST /users', level=2)
uc_create_headers = ['#', 'Test Method', 'Scenario', 'HTTP Status', 'Code']
uc_create_rows = [
    ['1', 'CreateUser_validRequest_success', 'Tạo user hợp lệ', '200', '1000'],
    ['2', 'CreateUser_usernameInvalid_fail', 'Username < 4 ký tự', '400', '1002'],
    ['3', 'CreateUser_passwordInvalid_fail', 'Password < 8 ký tự', '400', '1003'],
    ['4', 'CreateUser_userExisted_fail', 'Username đã tồn tại', '400', '1001'],
]
add_table_with_header(doc, uc_create_headers, uc_create_rows)
doc.add_paragraph()

doc.add_heading('5.2. GET /users', level=2)
uc_list_headers = ['#', 'Test Method', 'Authentication', 'Expected']
uc_list_rows = [
    ['1', 'GetUsers_validRequest_success', 'ADMIN', '200 + danh sách users'],
]
add_table_with_header(doc, uc_list_headers, uc_list_rows)
doc.add_paragraph()

doc.add_heading('5.3. GET /users/myInfo & GET /users/{id}', level=2)
uc_get_headers = ['#', 'Test Method', 'Endpoint', 'Scenario', 'Expected']
uc_get_rows = [
    ['1', 'GetMyInfo_validRequest_success', 'GET /users/myInfo', 'Lấy thông tin cá nhân', '200'],
    ['2', 'GetUserById_validRequest_success', 'GET /users/{id}', 'Tìm user theo ID', '200'],
    ['3', 'GetUserById_userNotFound_fail', 'GET /users/{id}', 'User không tồn tại', '404'],
]
add_table_with_header(doc, uc_get_headers, uc_get_rows)
doc.add_paragraph()

doc.add_heading('5.4. PUT & DELETE /users/{id}', level=2)
uc_mut_headers = ['#', 'Test Method', 'Endpoint', 'Authentication', 'Expected']
uc_mut_rows = [
    ['1', 'UpdateUser_validRequest_success', 'PUT /users/{id}', 'ADMIN', '200'],
    ['2', 'DeleteUser_success', 'DELETE /users/{id}', 'ADMIN', '200'],
]
add_table_with_header(doc, uc_mut_headers, uc_mut_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 6. AUTHENTICATIONCONTROLLER TEST
# ═══════════════════════════════════════════
h6 = doc.add_heading('6. AuthenticationController Test', level=1)
for run in h6.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_paragraph('Tổng số: 4 tests  |  Tỉ lệ pass: 100%')
doc.add_paragraph('File: src/test/java/com/example/identity_servive/controller/AuthenticationControllerTest.java')

ac_headers = ['#', 'Test Method', 'Endpoint', 'Scenario', 'Expected']
ac_rows = [
    ['1', 'authenticate_success', 'POST /auth/token', 'Đăng nhập thành công', '200 + code 2000'],
    ['2', 'introspect_success', 'POST /auth/introspect', 'Kiểm tra token hợp lệ', '200 + code 2000'],
    ['3', 'refreshToken_success', 'POST /auth/refresh', 'Refresh token', '200 + code 2000'],
    ['4', 'logout_success', 'POST /auth/logout', 'Đăng xuất', '200'],
]
add_table_with_header(doc, ac_headers, ac_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 7. SERVICE TESTS (NEW MODULES)
# ═══════════════════════════════════════════
h7a = doc.add_heading('7. Service Tests - Module mới', level=1)
for run in h7a.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_heading('7.1. Community Services (23 tests)', level=2)
doc.add_paragraph('File: PostServiceTest, FollowServiceTest, PostLikeServiceTest, CommentServiceTest, SavedPostServiceTest')
cs_headers = ['Service', 'Tests', 'Chức năng test']
cs_rows = [
    ['PostService', '3', 'createPost, getPosts, deletePost'],
    ['FollowService', '3', 'followUser, unfollowUser, getFollowers'],
    ['PostLikeService', '3', 'likePost, unlikePost, getLikes'],
    ['CommentService', '7', 'createComment, getComments, updateComment, deleteComment + fail cases'],
    ['SavedPostService', '7', 'savePost, unsavePost, getSavedPosts + permission checks'],
]
add_table_with_header(doc, cs_headers, cs_rows)
doc.add_paragraph()

doc.add_heading('7.2. Learning Services (7 tests)', level=2)
doc.add_paragraph('File: EnrollmentServiceTest, PracticeServiceTest, Judge0APIServiceTest')
ls_headers = ['Service', 'Tests', 'Chức năng test']
ls_rows = [
    ['EnrollmentService', '3', 'enrollCourse success + language/user not found fails'],
    ['PracticeService', '3', 'submit practice, complete practice, problem not found'],
    ['Judge0APIService', '1', 'execute Python code via remote API'],
]
add_table_with_header(doc, ls_headers, ls_rows)
doc.add_paragraph()

doc.add_heading('7.3. Other Services (8 tests)', level=2)
os_headers = ['Service', 'Tests', 'Chức năng test']
os_rows = [
    ['RoleService', '3', 'create, getAll, delete role'],
    ['PermissionService', '3', 'create, getAll, delete permission'],
    ['CloudinaryService', '2', 'uploadFile, deleteFile'],
]
add_table_with_header(doc, os_headers, os_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 8. CONTROLLER TESTS (NEW MODULES)
# ═══════════════════════════════════════════
h8a = doc.add_heading('8. Controller Tests - Module mới', level=1)
for run in h8a.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_heading('8.1. Community Controllers (32 tests)', level=2)
cm_headers = ['Controller', 'Tests', 'Chức năng test']
cm_rows = [
    ['CommunityController', '28', 'Posts CRUD, Comments CRUD, Like/Unlike, Follow/Unfollow, Save/Unsave + authorization checks'],
    ['AdminCommunityController', '4', 'Admin delete post/comment, admin management endpoints'],
]
add_table_with_header(doc, cm_headers, cm_rows)
doc.add_paragraph()

doc.add_heading('8.2. Learning Controllers (28 tests)', level=2)
lc_headers = ['Controller', 'Tests', 'Chức năng test']
lc_rows = [
    ['CourseController', '15', 'CRUD Steps, Problems, Lessons, Chapters, Languages (admin) + user read endpoints'],
    ['UserLearningController', '10', 'Get steps, problems, lessons, chapters, progress; verify lesson; submit practice; reset progress'],
    ['EnrollmentController', '2', 'Enroll to course, get enrollment info'],
    ['PistonAPIController', '1', 'Execute Python code via /api/v1/code/run'],
]
add_table_with_header(doc, lc_headers, lc_rows)
doc.add_paragraph()

doc.add_heading('8.3. AI Controller (3 tests)', level=2)
doc.add_paragraph('File: ChatControllerTest.java')
ai_headers = ['Test', 'Scenario', 'Expected']
ai_rows = [
    ['askQuestion_success', 'User gửi câu hỏi, AI trả lời', '200 + code 2000 + result != null'],
    ['explainCode_success', 'User gửi code, AI giải thích', '200 + code 2000 + result != null'],
    ['getHint_success', 'User yêu cầu gợi ý, AI trả hint', '200 + code 2000 + result != null'],
]
add_table_with_header(doc, ai_headers, ai_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 9. THỐNG KÊ TỔNG HỢP
# ═══════════════════════════════════════════
h7 = doc.add_heading('9. Thống Kê Tổng Hợp', level=1)
for run in h7.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

stat_headers = ['Module', 'Loại Test', 'Số Tests', 'File', 'Trạng thái']
stat_rows = [
    ['UserService', 'Service', '18', 'UserServiceTest.java', '✅ Hoàn thành'],
    ['AuthenticationService', 'Service', '8', 'AuthenticationServiceTest.java', '✅ Hoàn thành'],
    ['RoleService', 'Service', '3', 'RoleServiceTest.java', '✅ Hoàn thành'],
    ['PermissionService', 'Service', '3', 'PermissionServiceTest.java', '✅ Hoàn thành'],
    ['PostService', 'Service', '3', 'PostServiceTest.java', '✅ Hoàn thành'],
    ['FollowService', 'Service', '3', 'FollowServiceTest.java', '✅ Hoàn thành'],
    ['PostLikeService', 'Service', '3', 'PostLikeServiceTest.java', '✅ Hoàn thành'],
    ['CommentService', 'Service', '7', 'CommentServiceTest.java', '✅ Hoàn thành'],
    ['SavedPostService', 'Service', '7', 'SavedPostServiceTest.java', '✅ Hoàn thành'],
    ['CloudinaryService', 'Service', '2', 'CloudinaryServiceTest.java', '✅ Hoàn thành'],
    ['EnrollmentService', 'Service', '3', 'EnrollmentServiceTest.java', '✅ Hoàn thành'],
    ['PracticeService', 'Service', '3', 'PracticeServiceTest.java', '✅ Hoàn thành'],
    ['Judge0APIService', 'Service', '1', 'Judge0APIServiceTest.java', '✅ Hoàn thành'],
    ['UserController', 'Controller', '13', 'UserControllerTest.java', '✅ Hoàn thành'],
    ['AuthenticationController', 'Controller', '9', 'AuthenticationControllerTest.java', '✅ Hoàn thành'],
    ['RoleController', 'Controller', '3', 'RoleControllerTest.java', '✅ Hoàn thành'],
    ['PermissionController', 'Controller', '3', 'PermissionControllerTest.java', '✅ Hoàn thành'],
    ['CommunityController', 'Controller', '28', 'CommunityControllerTest.java', '✅ Hoàn thành'],
    ['AdminCommunityController', 'Controller', '4', 'AdminCommunityControllerTest.java', '✅ Hoàn thành'],
    ['CourseController', 'Controller', '15', 'CourseControllerTest.java', '✅ Hoàn thành'],
    ['EnrollmentController', 'Controller', '2', 'EnrollmentControllerTest.java', '✅ Hoàn thành'],
    ['PistonAPIController', 'Controller', '1', 'PistonAPIControllerTest.java', '✅ Hoàn thành'],
    ['UserLearningController', 'Controller', '10', 'UserLearningControllerTest.java', '✅ Hoàn thành'],
    ['ChatController', 'Controller', '3', 'ChatControllerTest.java', '✅ Hoàn thành'],
    ['UserControllerIntegration', 'Integration', '1', 'UserControllerIntegrationTest.java', '⚠️ Có lỗi'],
    ['Tổng cộng', '', '157', '26 files', '✅'],
]
add_table_with_header(doc, stat_headers, stat_rows)

doc.add_paragraph()

# Summary charts
doc.add_heading('Phân bố test theo layer', level=2)
pie_headers = ['Layer', 'Số lượng', 'Tỉ lệ']
pie_rows = [
    ['Service', '64 tests', '40.8%'],
    ['Controller', '92 tests', '58.6%'],
    ['Integration', '1 test', '0.6%'],
    ['Total', '157 tests', '100%'],
]
add_table_with_header(doc, pie_headers, pie_rows)

doc.add_page_break()

# ═══════════════════════════════════════════
# 8. HƯỚNG DẪN CHẠY TEST
# ═══════════════════════════════════════════
h8 = doc.add_heading('8. Hướng Dẫn Chạy Test', level=1)
for run in h8.runs:
    run.font.color.rgb = RGBColor(47, 84, 150)

doc.add_paragraph('Chạy tất cả tests:')
p = doc.add_paragraph()
run = p.add_run('mvn clean test')
run.font.name = 'Courier New'
run.font.size = Pt(11)
run.bold = True

doc.add_paragraph('Chạy test theo class cụ thể:')
p = doc.add_paragraph()
run = p.add_run('mvn test -Dtest=UserServiceTest')
run.font.name = 'Courier New'
run.font.size = Pt(11)
run.bold = True

doc.add_paragraph('Chạy test trong IntelliJ IDEA:')
doc.add_paragraph('• Click chuột phải vào file test → Run')
doc.add_paragraph('• Hoặc click vào icon Play bên cạnh class/method')

doc.add_paragraph('Lưu ý:')
doc.add_paragraph(
    '• Cần file test.properties ở src/test/resources với đầy đủ cấu hình\n'
    '• Các dependent service (Cloudinary) được mock để tránh lỗi context\n'
    '• Database test dùng H2 in-memory, không ảnh hưởng dữ liệu thật'
)

doc.add_page_break()

# ═══════════════════════════════════════════
# 9. TODO - FUTURE TESTS
# ═══════════════════════════════════════════
h9 = doc.add_heading('9. [TODO] Các Test Cần Bổ Sung', level=1)
for run in h9.runs:
    run.font.color.rgb = RGBColor(192, 0, 0)

doc.add_paragraph(
    'Các khu vực chưa có test, cần phát triển trong tương lai:'
)

todo_main_headers = ['STT', 'Module', 'Mô tả', 'Ưu tiên', 'Ghi chú']
todo_main_rows = [
    ['1', 'CloudinaryService', 'Upload file, xóa file', 'Thấp', 'Cần mock Cloudinary API'],
    ['2', 'UserService', 'updateAvatar() method', 'Trung bình', 'Method mới thêm'],
    ['3', 'AuthenticationController', 'outboundAuthenticate (Google OAuth)', 'Thấp', 'Cần mock Feign client'],
    ['4', 'UserController', 'upload-avatar endpoint', 'Trung bình', 'Multipart request'],
    ['5', 'GlobalExceptionHandler', 'Kiểm tra tất cả mã lỗi', 'Cao', 'Coverage exception mapping'],
    ['6', 'Integration Test', 'Test full flow với MySQL container (Testcontainers)', 'Trung bình', 'UserControllerIntegrationTest'],
    ['7', 'Security Test', 'Kiểm tra endpoint không được phép truy cập', 'Cao', 'Role-based access control'],
    ['8', 'Validation Test', 'Kiểm tra tất cả @Size, @Email, @DobConstraint', 'Trung bình', 'Boundary value analysis'],
]
add_table_with_header(doc, todo_main_headers, todo_main_rows)

doc.add_paragraph()
doc.add_heading('9.1. Controller Fail Cases còn thiếu', level=2)

cf_headers = ['#', 'Endpoint', 'Test Method', 'Scenario', 'Expected']
cf_rows = [
    ['1', 'POST /auth/token', 'authenticate_userNotFound_fail', 'Username không tồn tại', '400 + code 1005'],
    ['2', 'POST /auth/token', 'authenticate_wrongPassword_fail', 'Sai mật khẩu', '401 + code 1006'],
    ['3', 'POST /auth/introspect', 'introspect_invalidToken_fail', 'Token không hợp lệ', '200 + valid=false'],
    ['4', 'POST /auth/refresh', 'refreshToken_expired_fail', 'Token hết hạn', '401'],
    ['5', 'PUT /users/{id}', 'UpdateUser_userNotFound_fail', 'User ID không tồn tại', '404 + code 1005'],
    ['6', 'PUT /users/{id}', 'UpdateUser_notOwner_fail', 'User thường sửa của người khác', '403'],
    ['7', 'GET /users/myInfo', 'GetMyInfo_userNotFound_fail', 'User không tồn tại', '404 + code 1005'],
    ['8', 'GET /users/{id}', 'GetUserById_notFound_fail', 'User ID không tồn tại', '404 + code 1005'],
]
add_table_with_header(doc, cf_headers, cf_rows)

doc.add_paragraph()
doc.add_heading('9.2. AuthenticationController còn thiếu', level=2)

ac_todo_headers = ['#', 'Test Method', 'Endpoint', 'Scenario']
ac_todo_rows = [
    ['1', 'outboundAuthenticate_success', 'POST /auth/outbound/authentication?code=...', 'Google OAuth thành công'],
    ['2', 'authenticate_userNotFound_fail', 'POST /auth/token', 'User không tồn tại → 400'],
    ['3', 'authenticate_wrongPassword_fail', 'POST /auth/token', 'Sai mật khẩu → 401'],
    ['4', 'introspect_invalidToken_fail', 'POST /auth/introspect', 'Token rác → valid=false'],
    ['5', 'refreshToken_expired_fail', 'POST /auth/refresh', 'Token hết hạn → lỗi'],
]
add_table_with_header(doc, ac_todo_headers, ac_todo_rows)

doc.add_paragraph()
doc.add_heading('9.3. UserController còn thiếu (fail cases)', level=2)

uc_todo_headers = ['#', 'Test Method', 'Endpoint', 'Scenario']
uc_todo_rows = [
    ['1', 'GetMyInfo_userNotFound_fail', 'GET /users/myInfo', 'Token hợp lệ nhưng user không tồn tại'],
    ['2', 'UpdateUser_userNotFound_fail', 'PUT /users/{id}', 'Update user với ID không tồn tại'],
    ['3', 'UpdateUser_notOwner_fail', 'PUT /users/{id}', 'User thường sửa của người khác'],
    ['4', 'GetUserById_notFound_fail', 'GET /users/{id}', 'GET user với ID không tồn tại'],
]
add_table_with_header(doc, uc_todo_headers, uc_todo_rows)

doc.add_paragraph()
doc.add_heading('9.4. Module hoàn toàn chưa có test', level=2)

new_headers = ['#', 'Module', 'Loại', 'Mô tả', 'Gợi ý']
new_rows = [
    ['1', 'GlobalExceptionHandler', 'Unit Test', 'Kiểm tra tất cả exception → HTTP status mapping', 'Từng ErrorCode + từng exception type'],
    ['2', 'CustomJwtDecoder', 'Unit Test', 'Decode JWT, verify token', 'Tạo token thật, mock introspect'],
    ['3', 'SecurityConfig + Filter Chain', 'Integration Test', 'Endpoint không cho phép truy cập nếu không có role', 'Gọi API không token, sai role'],
    ['4', 'UserControllerIntegrationTest', 'Integration Test', 'Full flow với MySQL thật (Testcontainers)', 'Dùng @Container MySQL'],
    ['5', 'Validation Boundary Tests', 'Unit Test', 'Kiểm tra @Size(min=4), @Email, @DobConstraint', 'Boundary values (min, max, null)'],
    ['6', 'CloudinaryService', 'Unit Test', 'Upload file, xóa file', 'Mock Cloudinary API'],
]
add_table_with_header(doc, new_headers, new_rows)

# ─── Footer ───
doc.add_paragraph()
footer = doc.add_paragraph()
footer.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = footer.add_run('--- Hết ---')
run.font.size = Pt(14)
run.font.color.rgb = RGBColor(150, 150, 150)

# ─── Save ───
output_path = os.path.join(os.path.dirname(__file__), 'BaoCao_Test_Coverage.docx')
doc.save(output_path)
print(f'Report saved to: {output_path}')
