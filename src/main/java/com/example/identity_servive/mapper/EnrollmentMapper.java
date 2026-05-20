package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.learningRequest.EnrollmentRequest;
import com.example.identity_servive.dto.response.progress.EnrollmentResponse;
import com.example.identity_servive.entity.progress.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    Enrollment toEnrollment(EnrollmentRequest request);
    EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);
}
