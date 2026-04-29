package com.example.identity_servive.mapper;

import com.example.identity_servive.dto.request.EnrollmentRequest;
import com.example.identity_servive.dto.response.EnrollmentResponse;
import com.example.identity_servive.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    Enrollment toEnrollment(EnrollmentRequest request);
    EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);
}
