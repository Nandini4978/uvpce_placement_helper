package com.example.uvpce_placement_helper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class AdminDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminSignupRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @NotBlank(message = "Password confirmation is required")
        private String confirmPassword;

        private String name;
        private String department;
        private String designation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminResponse {
        private Long id;
        private String email;
        private String name;
        private String department;
        private String designation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalRequest {
        @NotBlank(message = "User ID is required")
        private Long userId;

        @NotBlank(message = "Approval status is required")
        private boolean approved;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BroadcastRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Message is required")
        private String message;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentShortlistRequest {
        @NotBlank(message = "Drive ID is required")
        private Long driveId;

        @NotBlank(message = "Student IDs are required")
        private List<Long> studentIds;
    }
}