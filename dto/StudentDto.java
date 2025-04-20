package com.example.uvpce_placement_helper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.util.List;

public class StudentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentSignupRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @NotBlank(message = "Password confirmation is required")
        private String confirmPassword;

        @NotBlank(message = "Enrollment number is required")
        private String enrollmentNumber;

        @NotNull(message = "CGPA is required")
        @DecimalMin(value = "0.0", message = "CGPA can't be negative")
        @DecimalMax(value = "10.0", message = "CGPA can't be more than 10.0")
        private Double cgpa;

        private String resumeUrl;
        private String linkedinUrl;
        private String githubUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentProfileUpdateRequest {
        private Double cgpa;
        private String resumeUrl;
        private String linkedinUrl;
        private String githubUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentResponse {
        private Long id;
        private String email;
        private String enrollmentNumber;
        private Double cgpa;
        private String resumeUrl;
        private String linkedinUrl;
        private String githubUrl;
        private boolean isApproved;
        private List<Long> appliedDriveIds;
        private List<Long> shortlistedDriveIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DriveApplicationRequest {
        @NotNull(message = "Drive ID is required")
        private Long driveId;
    }
}