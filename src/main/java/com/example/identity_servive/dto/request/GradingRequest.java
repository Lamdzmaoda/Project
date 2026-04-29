package com.example.identity_servive.dto.request;

import lombok.*;

/**
 * Request để chấm bài tự luận/code
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradingRequest {
    
    /**
     * Câu hỏi/đề bài
     */
    private String question;
    
    /**
     * Bài làm của học sinh
     */
    private String studentAnswer;
    
    /**
     * Đáp án mẫu (dành cho tự luận)
     */
    private String modelAnswer;
    
    /**
     * Loại bài: essay (tự luận), code (lập trình), mcq (trắc nghiệm)
     */
    private String assignmentType;
    
    /**
     * Rubric chấm điểm (optional)
     */
    private String rubric;
    
    /**
     * Điểm tối đa
     */
    private Integer maxScore = 10;
}
