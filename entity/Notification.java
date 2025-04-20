package com.example.uvpce_placement_helper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    // If studentId is null, it's a broadcast message
    private Long studentId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean isRead = false;
}