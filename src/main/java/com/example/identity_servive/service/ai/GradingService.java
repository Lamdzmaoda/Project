//package com.example.identity_servive.service;
//
//import com.example.identity_servive.dto.response.GradingResponse;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Service chấm bài tự động sử dụng AI (theo kiến trúc MarkMate)
// *
// * Architecture:
// * Controller → GradingService → Spring AI (LLM) → Response
// */
//@Service
//@Slf4j
//public class GradingService {
//
//    private final ChatClient chatClient;
//    private final ObjectMapper objectMapper;
//
//    public GradingService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
//        this.chatClient = chatClientBuilder.build();
//        this.objectMapper = objectMapper;
//    }
//
//    /**
//     * Chấm bài tự luận
//     *
//     * @param question       Câu hỏi
//     * @param studentAnswer  Bài làm của học sinh
//     * @param modelAnswer    Đáp án mẫu
//     * @param rubric         Rubric chấm điểm (optional)
//     * @param maxScore       Điểm tối đa
//     * @return Kết quả chấm dưới dạng JSON
//     */
//    public String gradeEssay(String question, String studentAnswer, String modelAnswer,
//                             String rubric, Integer maxScore) {
//
//        String systemPrompt = """
//            Bạn là một trợ lý chấm bài chuyên nghiệp và công bằng.
//            Nhiệm vụ của bạn là chấm điểm bài làm của học sinh dựa trên câu hỏi, đáp án mẫu và rubric provided.
//
//            Yêu cầu:
//            1. Phân tích kỹ bài làm của học sinh
//            2. So sánh với đáp án mẫu
//            3. Áp dụng rubric nếu có
//            4. Cho điểm công bằng, khách quan
//            5. Cung cấp feedback chi tiết, xây dựng
//            6. Chỉ ra những ý đúng/sai/thiếu
//            7. Đưa ra gợi ý cải thiện cụ thể
//
//            Output BẮT BUỘC phải theo format JSON sau:
//            {
//                "score": <số nguyên>,
//                "feedback": "<nhận xét chi tiết>",
//                "correctPoints": ["<ý đúng 1>", "<ý đúng 2>"],
//                "incorrectPoints": ["<ý sai/thiếu 1>", "<ý sai/thiếu 2>"],
//                "suggestions": ["<gợi ý 1>", "<gợi ý 2>"]
//            }
//            """;
//
//        String userPrompt = String.format("""
//            **Câu hỏi:**
//            %s
//
//            **Đáp án mẫu:**
//            %s
//
//            **Bài làm của học sinh:**
//            %s
//
//            **Rubric chấm điểm:**
//            %s
//
//            **Điểm tối đa:** %d
//
//            Hãy chấm điểm và trả về kết quả theo format JSON đã yêu cầu.
//            """,
//            question != null ? question : "N/A",
//            modelAnswer != null ? modelAnswer : "N/A",
//            studentAnswer != null ? studentAnswer : "Trống",
//            rubric != null ? rubric : "Không có rubric cụ thể",
//            maxScore != null ? maxScore : 100
//        );
//
//        log.info("Bắt đầu chấm bài essay - Question length: {}, Answer length: {}",
//                 question != null ? question.length() : 0,
//                 studentAnswer != null ? studentAnswer.length() : 0);
//
//        String response = chatClient.prompt(new Prompt(systemPrompt, userPrompt))
//                .call()
//                .content();
//
//        log.info("Hoàn thành chấm bài - Response length: {}", response.length());
//
//        return response;
//    }
//
//    /**
//     * Chấm bài code lập trình
//     *
//     * @param problemDescription Mô tả bài toán
//     * @param expectedOutput     Output mong đợi
//     * @param studentCode        Code của học sinh
//     * @param programmingLanguage Ngôn ngữ lập trình
//     * @param maxScore           Điểm tối đa
//     * @return Kết quả chấm dưới dạng JSON
//     */
//    public String gradeCode(String problemDescription, String expectedOutput,
//                           String studentCode, String programmingLanguage, Integer maxScore) {
//
//        String systemPrompt = """
//            Bạn là một chuyên gia chấm code lập trình với nhiều năm kinh nghiệm.
//            Nhiệm vụ của bạn là đánh giá code của học sinh một cách toàn diện.
//
//            Các tiêu chí đánh giá:
//            1. **Correctness**: Code có chạy đúng không? Có xử lý edge cases không?
//            2. **Efficiency**: Độ phức tạp thời gian/không gian có tối ưu không?
//            3. **Code Quality**: Code có sạch, dễ đọc, đặt tên tốt không?
//            4. **Best Practices**: Có tuân thủ best practices của ngôn ngữ không?
//            5. **Error Handling**: Có xử lý lỗi đầy đủ không?
//
//            Output BẮT BUỘC phải theo format JSON sau:
//            {
//                "score": <số nguyên>,
//                "feedback": "<nhận xét chi tiết>",
//                "correctPoints": ["<ưu điểm 1>", "<ưu điểm 2>"],
//                "incorrectPoints": ["<lỗi 1>", "<lỗi 2>"],
//                "suggestions": ["<gợi ý cải thiện 1>", "<gợi ý 2>"]
//            }
//            """;
//
//        String userPrompt = String.format("""
//            **Mô tả bài toán:**
//            %s
//
//            **Output mong đợi/Test cases:**
//            %s
//
//            **Code của học sinh (%s):**
//            ```%s
//            %s
//            ```
//
//            **Điểm tối đa:** %d
//
//            Hãy phân tích code và chấm điểm theo format JSON đã yêu cầu.
//            """,
//            problemDescription != null ? problemDescription : "N/A",
//            expectedOutput != null ? expectedOutput : "N/A",
//            programmingLanguage != null ? programmingLanguage : "Unknown",
//            programmingLanguage != null ? programmingLanguage : "text",
//            studentCode != null ? studentCode : "Trống",
//            maxScore != null ? maxScore : 100
//        );
//
//        log.info("Bắt đầu chấm bài code - Language: {}, Code length: {}",
//                 programmingLanguage,
//                 studentCode != null ? studentCode.length() : 0);
//
//        String response = chatClient.prompt(new Prompt(systemPrompt, userPrompt))
//                .call()
//                .content();
//
//        log.info("Hoàn thành chấm bài code - Response length: {}", response.length());
//
//        return response;
//    }
//
//    /**
//     * Chấm bài trắc nghiệm (MCQ)
//     * Đơn giản hơn vì chỉ cần so sánh đáp án
//     */
//    public String gradeMCQ(String questions, String studentAnswers, String correctAnswers,
//                          Integer maxScore) {
//
//        String systemPrompt = """
//            Bạn là trợ lý chấm bài trắc nghiệm.
//            Nhiệm vụ: So sánh đáp án của học sinh với đáp án đúng và cho điểm.
//
//            Output BẮT BUỘC theo format JSON:
//            {
//                "score": <số nguyên>,
//                "feedback": "<nhận xét>",
//                "correctPoints": ["<câu đúng 1>", "..."],
//                "incorrectPoints": ["<câu sai 1>", "..."],
//                "suggestions": ["<gợi ý ôn tập>"]
//            }
//            """;
//
//        String userPrompt = String.format("""
//            **Danh sách câu hỏi:**
//            %s
//
//            **Đáp án của học sinh:**
//            %s
//
//            **Đáp án đúng:**
//            %s
//
//            **Điểm tối đa:** %d
//
//            Hãy chấm điểm và trả về JSON.
//            """,
//            questions != null ? questions : "N/A",
//            studentAnswers != null ? studentAnswers : "Trống",
//            correctAnswers != null ? correctAnswers : "N/A",
//            maxScore != null ? maxScore : 100
//        );
//
//        log.info("Bắt đầu chấm bài MCQ");
//
//        String response = chatClient.prompt(new Prompt(systemPrompt, userPrompt))
//                .call()
//                .content();
//
//        log.info("Hoàn thành chấm bài MCQ - Response length: {}", response.length());
//
//        return response;
//    }
//
//    /**
//     * Parse JSON response từ AI thành GradingResponse object
//     *
//     * @param aiResponse Raw response từ AI
//     * @return GradingResponse đã parse
//     */
//    public GradingResponse parseGradingResult(String aiResponse) {
//        try {
//            log.debug("Parsing AI response: {}", aiResponse);
//
//            // Trích xuất JSON từ response (nếu AI trả về markdown code block)
//            String jsonContent = extractJsonFromResponse(aiResponse);
//
//            JsonNode rootNode = objectMapper.readTree(jsonContent);
//
//            GradingResponse.GradedResponseBuilder builder = GradingResponse.builder()
//                    .gradedAt(System.currentTimeMillis())
//                    .rawResponse(aiResponse);
//
//            if (rootNode.has("score")) {
//                builder.score(rootNode.get("score").asInt());
//            }
//
//            if (rootNode.has("feedback")) {
//                builder.feedback(rootNode.get("feedback").asText());
//            }
//
//            if (rootNode.has("correctPoints") && rootNode.get("correctPoints").isArray()) {
//                List<String> correctPoints = new ArrayList<>();
//                rootNode.get("correctPoints").forEach(node ->
//                    correctPoints.add(node.asText())
//                );
//                builder.correctPoints(correctPoints);
//            }
//
//            if (rootNode.has("incorrectPoints") && rootNode.get("incorrectPoints").isArray()) {
//                List<String> incorrectPoints = new ArrayList<>();
//                rootNode.get("incorrectPoints").forEach(node ->
//                    incorrectPoints.add(node.asText())
//                );
//                builder.incorrectPoints(incorrectPoints);
//            }
//
//            if (rootNode.has("suggestions") && rootNode.get("suggestions").isArray()) {
//                List<String> suggestions = new ArrayList<>();
//                rootNode.get("suggestions").forEach(node ->
//                    suggestions.add(node.asText())
//                );
//                builder.suggestions(suggestions);
//            }
//
//            GradingResponse response = builder.build();
//            log.info("Parsed grading result - Score: {}", response.getScore());
//
//            return response;
//
//        } catch (Exception e) {
//            log.error("Error parsing grading response: {}", e.getMessage(), e);
//
//            // Fallback: trả về response với raw content
//            return GradingResponse.builder()
//                    .feedback(aiResponse)
//                    .gradedAt(System.currentTimeMillis())
//                    .rawResponse(aiResponse)
//                    .build();
//        }
//    }
//
//    /**
//     * Trích xuất JSON từ response của AI
//     * (AI đôi khi trả về markdown code block ```json ... ```)
//     */
//    private String extractJsonFromResponse(String response) {
//        if (response == null || response.isEmpty()) {
//            return "{}";
//        }
//
//        // Tìm JSON trong markdown code block
//        int jsonStart = response.indexOf("{");
//        int jsonEnd = response.lastIndexOf("}");
//
//        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
//            return response.substring(jsonStart, jsonEnd + 1);
//        }
//
//        // Nếu không tìm thấy, trả về nguyên bản
//        return response.trim();
//    }
//}
