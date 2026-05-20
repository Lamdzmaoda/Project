package com.example.identity_servive.dto.response.learningResponse;

import lombok.*;
import java.util.List;

/**
 * Response kết quả chấm bài từ AI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradingResponse {
    
    /**
     * Điểm số (0-100 hoặc theo maxScore)
     */
    private Integer score;
    
    /**
     * Nhận xét chi tiết
     */
    private String feedback;
    
    /**
     * Các ý đúng trong bài làm
     */
    private List<String> correctPoints;
    
    /**
     * Các ý sai/thiếu
     */
    private List<String> incorrectPoints;
    
    /**
     * Gợi ý cải thiện
     */
    private List<String> suggestions;
    
    /**
     * Thời gian chấm
     */
    private Long gradedAt;
    
    /**
     * Raw response từ AI (dùng cho debugging)
     */
    private String rawResponse;
}
