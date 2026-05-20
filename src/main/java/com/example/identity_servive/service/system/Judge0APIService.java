package com.example.identity_servive.service.system;

import com.example.identity_servive.dto.request.ai.CodeRequest;
import com.example.identity_servive.dto.response.ai.CodeResponse;
import com.example.identity_servive.entity.System.Code;
import com.example.identity_servive.enums.Status;
import com.example.identity_servive.mapper.CodeMapper;
import com.example.identity_servive.mapper.UserMapper;
import com.example.identity_servive.repository.learning.CodeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service xử lý thực thi mã nguồn Python duy nhất bằng Judge0 API.
 * Hỗ trợ bắt lỗi chi tiết (số dòng, loại lỗi) và giải mã Base64.
 */
@Slf4j // Hỗ trợ ghi lại lịch sử hoạt động (Logging)
@Service // Đăng ký lớp này là một Service do Spring quản lý (Bean)
@RequiredArgsConstructor // Tự động tạo Constructor để tiêm (Inject) các Repository và Mapper vào
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Judge0APIService {
    CodeRepository codeRepository;
    CodeMapper codeMapper;
    // --- CẤU HÌNH JUDGE0 API ---
    // Sử dụng Public Instance của Judge0 (Miễn phí và ổn định)
    private final String JUDGE0_URL = "https://ce.judge0.com/submissions?base64_encoded=true&wait=true";
    private final UserMapper userMapper;

    /**
     * Thực thi code Python từ Client gửi lên thông qua Judge0.
     * @param sourceCode Chuỗi mã nguồn Python cần chạy.
     * @return Map chứa kết quả đầu ra (output) hoặc lỗi chi tiết.
     */
    /**
     * Hàm thực thi code Python thông qua dịch vụ chấm thi Judge0.
     * @param sourceCode Đoạn code Python nguyên bản từ người dùng gửi lên.
     * @return Một Map chứa kết quả đầu ra (output) hoặc thông báo lỗi.
     */
    public CodeResponse executePythonCode(CodeRequest sourceCode) {

        // 1. Cấu hình thời gian chờ (Timeout) để tránh treo hệ thống
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // Chờ kết nối tối đa 10 giây
        factory.setReadTimeout(10000);    // Chờ phản hồi tối đa 10 giây
        Code codeEntity = codeMapper.toCode(sourceCode);

        // RestTemplate là công cụ dùng để gọi API từ bên ngoài vào Java
        RestTemplate restTemplate = new RestTemplate(factory);

        // 2. Thiết lập Header cho Request (báo cho Server biết mình gửi dữ liệu dạng JSON)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. MÃ HÓA CODE: Judge0 yêu cầu code phải được chuyển sang Base64
        // để giữ nguyên các khoảng trắng và dấu xuống dòng của Python.
        String encodedCode = Base64.getEncoder().encodeToString(sourceCode.getInput().getBytes());
        codeEntity.setInput(encodedCode);
        // 4. CHUẨN BỊ BODY: Tạo gói dữ liệu gửi đi
        Map<String, Object> body = new HashMap<>();
        body.put("source_code", encodedCode);
        body.put("language_id", 71); // 71 là mã định danh cho Python 3.8.1 trên Judge0

        // Gói cả Body và Header vào một thực thể HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            // 5. GỬI REQUEST: Sử dụng phương thức POST để gửi code tới máy chủ Judge0
            // JUDGE0_URL thường có dạng: https://api.judge0.com/submissions?base64_encoded=true&wait=true
            Map<String, Object> response = restTemplate.postForObject(JUDGE0_URL, entity, Map.class);

            if (response != null) {
                String stdout = (String) response.get("stdout");
                String stderr = (String) response.get("stderr");
                String compileOut = (String) response.get("compile_output");
                if((stderr != null && !stderr.isEmpty()) || (compileOut != null && !compileOut.isEmpty())) {
                    String rawError = decode( stderr != null && !stderr.isEmpty() ? stderr : compileOut );

                    parseAndFormatError(rawError ,codeEntity);
                    codeEntity.setStatus(Status.ERROR);
                }else {
                    codeEntity.setStatus(Status.SUCCESS);
                    codeEntity.setOutput(decode( stdout ) );

                }
                codeRepository.save(codeEntity);
                return codeMapper.toCodeResponse(codeEntity);
            }
            throw new RuntimeException("Không nhận được phản hồi từ Judge0");
        }catch (Exception e) {
            log.error("Lỗi thực thi: ", e);
            codeEntity.setOutput("Lỗi hệ thống: " + e.getMessage());
            codeEntity.setStatus(Status.ERROR);
            codeRepository.save(codeEntity);
            return codeMapper.toCodeResponse(codeEntity);
        }
    }

    /**
     * Giải mã chuỗi Base64 nhận được từ Judge0 quay trở lại thành văn bản bình thường.
     */
    private String decode(String base64Str) {
        try {
            // Dùng thư viện Base64 của Java để giải mã
            return new String(Base64.getDecoder().decode(base64Str.trim()));
        } catch (Exception e) {
            return "[Lỗi giải mã dữ liệu]";
        }
    }
    private void parseAndFormatError(String rawError, Code entity) {
        Pattern linePattern = Pattern.compile("line (\\d+)");
        Matcher matcher = linePattern.matcher(rawError);

        if (matcher.find()) {
            entity.setLine(Integer.parseInt(matcher.group(1)));
        }

        String[] lines = rawError.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // Tìm vị trí lỗi (dấu ^)
            if (line.contains("^")) {
                entity.setPointer(line);
                int columnIndex = line.indexOf("^");
                entity.setColumnIndex(columnIndex);
                // Dòng ngay phía trên dấu ^ chính là dòng code bị lỗi
                if (i > 0) {
                    entity.setErrorLineCode(lines[i - 1].trim());
                }
            }

            // Tìm loại lỗi và dịch
            if (line.contains("Error")) {
                String type = line.split(":")[0].trim();
                entity.setErrorType(type);
                entity.setMessageVn(translateError(line));
            }
        }
    }
    /**
     * Hiển thị kết quả ra Console Log để Debug dễ dàng hơn.
     */
    private String translateError(String line) {
        Map<String, String> errorDictionary = new HashMap<>();
        errorDictionary.put("SyntaxError", "Lỗi cú pháp");
        errorDictionary.put("IndentationError", "Lỗi thụt lề (khoảng cách)");
        errorDictionary.put("NameError", "Lỗi tên biến hoặc hàm không tồn tại");
        errorDictionary.put("TypeError", "Lỗi kiểu dữ liệu");
        errorDictionary.put("IndexError", "Lỗi chỉ số (vượt quá độ dài danh sách)");
        errorDictionary.put("KeyError", "Lỗi không tìm thấy khóa trong Dictionary");
        errorDictionary.put("ValueError", "Lỗi giá trị không hợp lệ");
        errorDictionary.put("ZeroDivisionError", "Lỗi chia cho số 0");

        for(Map.Entry<String, String> entry : errorDictionary.entrySet()) {
            if(line.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "Lỗi không xác định: " + line;
    }
    private void logToConsole(String output) {
        System.out.println("\n========= [KIỂM TRA CODE PYTHON] =========");
        System.out.println(output);
        System.out.println("==========================================\n");
    }
}