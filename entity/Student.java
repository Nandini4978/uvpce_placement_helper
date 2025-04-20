package com.example.uvpce_placement_helper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student extends User {

    @Column(unique = true, nullable = false)
    private String enrollmentNumber;

    private Double cgpa;

    @Column(columnDefinition = "TEXT")
    private String resumeUrl;

    private String linkedinUrl;

    private String githubUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_applications",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "drive_id")
    )
    private List<PlacementDrive> appliedDrives = new ArrayList<>();

    // Shortlisted flag for admin to mark a student
    @ElementCollection
    @CollectionTable(name = "shortlisted_students",
            joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "drive_id")
    private List<Long> shortlistedForDrives = new ArrayList<>();
}