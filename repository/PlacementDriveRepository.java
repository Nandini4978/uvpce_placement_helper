package com.example.uvpce_placement_helper.repository;

import com.example.uvpce_placement_helper.entity.PlacementDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlacementDriveRepository extends JpaRepository<PlacementDrive, Long> {
    List<PlacementDrive> findByCompanyId(Long companyId);

    List<PlacementDrive> findByIsActiveTrue();

    @Query("SELECT p FROM PlacementDrive p WHERE p.applicationDeadline >= :today AND p.isActive = true")
    List<PlacementDrive> findActiveWithDeadlineAfter(LocalDate today);

    @Query("SELECT p FROM PlacementDrive p WHERE p.minimumCgpa <= :cgpa AND p.applicationDeadline >= :today AND p.isActive = true")
    List<PlacementDrive> findEligibleDrivesForStudent(Double cgpa, LocalDate today);
}