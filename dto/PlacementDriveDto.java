package com.example.uvpce_placement_helper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class PlacementDriveDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacementDriveRequest {
        @NotBlank(message = "Title is required")
        private String title;

        private String location;

        @NotNull(message = "Package offered is required")
        @DecimalMin(value = "0.0", message = "Package can't be negative")
        private Double packageOffered;

        private String techStack;

        @NotNull(message = "Number of openings is required")
        @Min(value = 1, message = "Openings must be at least 1")
        private Integer openings;

        private String jobDescription;

        private Boolean hasBond = false;

        private Integer bondDuration;

        @NotNull(message = "Minimum CGPA is required")
        @DecimalMin(value = "0.0", message = "CGPA can't be negative")
        @DecimalMax(value = "10.0", message = "CGPA can't be more than 10.0")
        private Double minimumCgpa;

        @NotNull(message = "Application deadline is required")
        private LocalDate applicationDeadline;

        @NotNull(message = "Drive date is required")
        private LocalDate driveDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacementDriveResponse {
        private Long id;
        private String title;
        private Long companyId;
        private String companyName;
        private String location;
        private Double packageOffered;
        private String techStack;
        private Integer openings;
        private String jobDescription;
        private Boolean hasBond;
        private Integer bondDuration;
        private Double minimumCgpa;
        private LocalDate applicationDeadline;
        private LocalDate driveDate;
        private Boolean isActive;
        private Integer applicantCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacementDriveUpdateRequest {
        private String title;
        private String location;
        private Double packageOffered;
        private String techStack;
        private Integer openings;
        private String jobDescription;
        private Boolean hasBond;
        private Integer bondDuration;
        private Double minimumCgpa;
        private LocalDate applicationDeadline;
        private LocalDate driveDate;
        private Boolean isActive;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicantResponse {
        private List<StudentDto.StudentResponse> students;
    }
}