package com.example.uvpce_placement_helper.repository;

import com.example.uvpce_placement_helper.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEnrollmentNumber(String enrollmentNumber);

    List<Student> findByIsApprovedFalse();

    @Query("SELECT s FROM Student s WHERE s.cgpa >= :minCgpa")
    List<Student> findEligibleStudents(Double minCgpa);

    @Query("SELECT s FROM Student s JOIN s.appliedDrives d WHERE d.id = :driveId")
    List<Student> findStudentsByDriveId(Long driveId);
}