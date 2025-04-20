package com.example.uvpce_placement_helper.service;

import com.example.uvpce_placement_helper.dto.StudentDto;
import com.example.uvpce_placement_helper.entity.PlacementDrive;
import com.example.uvpce_placement_helper.entity.Student;
import com.example.uvpce_placement_helper.repository.PlacementDriveRepository;
import com.example.uvpce_placement_helper.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PlacementDriveRepository driveRepository;

    @Autowired
    private NotificationService notificationService;

    public StudentDto.StudentResponse getStudentProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return mapToStudentResponse(student);
    }

    public StudentDto.StudentResponse updateStudentProfile(Long studentId, StudentDto.StudentProfileUpdateRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (request.getCgpa() != null) {
            student.setCgpa(request.getCgpa());
        }

        if (request.getResumeUrl() != null) {
            student.setResumeUrl(request.getResumeUrl());
        }

        if (request.getLinkedinUrl() != null) {
            student.setLinkedinUrl(request.getLinkedinUrl());
        }

        if (request.getGithubUrl() != null) {
            student.setGithubUrl(request.getGithubUrl());
        }

        studentRepository.save(student);
        return mapToStudentResponse(student);
    }

    public List<PlacementDrive> getEligibleDrives(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return driveRepository.findEligibleDrivesForStudent(student.getCgpa(), LocalDate.now());
    }

    public void applyForDrive(Long studentId, Long driveId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Placement drive not found"));

        // Check if student has already applied
        if (student.getAppliedDrives().stream().anyMatch(d -> d.getId().equals(driveId))) {
            throw new RuntimeException("You have already applied for this drive");
        }

        // Check eligibility
        if (student.getCgpa() < drive.getMinimumCgpa()) {
            throw new RuntimeException("You do not meet the minimum CGPA requirement");
        }

        // Check if application deadline has passed
        if (LocalDate.now().isAfter(drive.getApplicationDeadline())) {
            throw new RuntimeException("Application deadline has passed");
        }

        student.getAppliedDrives().add(drive);
        studentRepository.save(student);

        // Create notification for student
        notificationService.createNotification(
                "Application Submitted",
                "You have successfully applied for " + drive.getTitle() + " at " + drive.getCompany().getName(),
                studentId);
    }

    public List<PlacementDrive> getAppliedDrives(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return student.getAppliedDrives();
    }

    private StudentDto.StudentResponse mapToStudentResponse(Student student) {
        List<Long> appliedDriveIds = student.getAppliedDrives().stream()
                .map(PlacementDrive::getId)
                .collect(Collectors.toList());

        return new StudentDto.StudentResponse(
                student.getId(),
                student.getEmail(),
                student.getEnrollmentNumber(),
                student.getCgpa(),
                student.getResumeUrl(),
                student.getLinkedinUrl(),
                student.getGithubUrl(),
                student.isApproved(),
                appliedDriveIds,
                student.getShortlistedForDrives()
        );
    }
}