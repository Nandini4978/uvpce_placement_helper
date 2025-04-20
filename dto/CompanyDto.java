package com.example.uvpce_placement_helper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CompanyDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanySignupRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @NotBlank(message = "Password confirmation is required")
        private String confirmPassword;

        @NotBlank(message = "Company name is required")
        private String name;

        private String location;
        private String website;
        private String industry;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyResponse {
        private Long id;
        private String email;
        private String name;
        private String location;
        private String website;
        private String industry;
        private String description;
        private boolean isApproved;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyProfileUpdateRequest {
        private String name;
        private String location;
        private String website;
        private String industry;
        private String description;
    }
}