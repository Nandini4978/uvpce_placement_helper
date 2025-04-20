package com.example.uvpce_placement_helper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class NotificationDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Message is required")
        private String message;

        // If studentId is null, it's a broadcast notification
        private Long studentId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResponse {
        private Long id;
        private String title;
        private String message;
        private LocalDateTime createdAt;
        private boolean isRead;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationReadRequest {
        private Long notificationId;
    }
}