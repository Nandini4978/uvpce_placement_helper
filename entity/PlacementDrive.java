package com.example.uvpce_placement_helper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "placement_drives")
public class PlacementDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

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

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean isActive = true;

    @ManyToMany(mappedBy = "appliedDrives")
    private List<Student> applicants = new ArrayList<>();
}