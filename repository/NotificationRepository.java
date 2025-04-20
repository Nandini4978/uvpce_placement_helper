package com.example.uvpce_placement_helper.repository;

import com.example.uvpce_placement_helper.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<Notification> findByStudentIdIsNullOrderByCreatedAtDesc();

    List<Notification> findByStudentIdAndIsReadFalse(Long studentId);

    List<Notification> findByStudentIdIsNullAndIsReadFalse();
}