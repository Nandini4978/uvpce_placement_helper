package com.example.uvpce_placement_helper.service;

import com.example.uvpce_placement_helper.dto.AdminDto;
import com.example.uvpce_placement_helper.dto.PlacementDriveDto;
import com.example.uvpce_placement_helper.entity.*;
import com.example.uvpce_placement_helper.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlacementDriveRepository driveRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    public AdminDto.AdminResponse getAdminProfile(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return new AdminDto.AdminResponse(
                admin.getId(),
                admin.getEmail(),
                admin.getName(),
                admin.getDepartment(),
                admin.getDesignation()
        );
    }

    public List<Student> getPendingStudentApprovals() {
        return studentRepository.findByIsApprovedFalse();
    }

    public List<Company> getPendingCompanyApprovals() {
        return companyRepository.findByIsApprovedFalse();
    }

    public void approveUser(Long userId, boolean approved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setApproved(approved);
        userRepository.save(user);

        // Send approval email notification
        emailService.sendAccountApprovalEmail(user.getEmail(), approved);

        // Create notification
        String title = approved ? "Account Approved" : "Account Rejected";
        String message = approved
                ? "Your account has been approved. You can now access all features."
                : "Your account has been rejected. Please contact the admin for more information.";

        if (user instanceof Student) {
            notificationService.createNotification(title, message, userId);
        }
    }

    public void addPlacementDrive(PlacementDriveDto.PlacementDriveRequest request, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        PlacementDrive drive = new PlacementDrive();
        drive.setTitle(request.getTitle());
        drive.setCompany(company);
        drive.setLocation(request.getLocation());
        drive.setPackageOffered(request.getPackageOffered());
        drive.setTechStack(request.getTechStack());
        drive.setOpenings(request.getOpenings());
        drive.setJobDescription(request.getJobDescription());
        drive.setHasBond(request.getHasBond());
        drive.setBondDuration(request.getBondDuration());
        drive.setMinimumCgpa(request.getMinimumCgpa());
        drive.setApplicationDeadline(request.getApplicationDeadline());
        drive.setDriveDate(request.getDriveDate());

        driveRepository.save(drive);

        // Create broadcast notification for all students
        notificationService.createBroadcastNotification(
                "New Placement Drive",
                "A new placement drive has been posted: " + request.getTitle() + " by " + company.getName());
    }

    public void shortlistStudents(Long driveId, List<Long> studentIds) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Placement drive not found"));

        for (Long studentId : studentIds) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

            // Add to shortlisted list if not already there
            if (!student.getShortlistedForDrives().contains(driveId)) {
                student.getShortlistedForDrives().add(driveId);
                studentRepository.save(student);

                // Send email notification
                emailService.sendSelectionEmail(student.getEmail(), drive.getCompany().getName(), drive.getTitle());

                // Create notification
                notificationService.createNotification(
                        "Shortlisted for " + drive.getTitle(),
                        "Congratulations! You have been shortlisted for " + drive.getTitle() + " at " + drive.getCompany().getName(),
                        studentId);
            }
        }
    }

    public void broadcastNotification(String title, String message) {
        notificationService.createBroadcastNotification(title, message);
    }

    public void updatePlacementDrive(Long driveId, PlacementDriveDto.PlacementDriveUpdateRequest request) {
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Placement drive not found"));

        if (request.getTitle() != null) {
            drive.setTitle(request.getTitle());
        }

        if (request.getLocation() != null) {
            drive.setLocation(request.getLocation());
        }

        if (request.getPackageOffered() != null) {
            drive.setPackageOffered(request.getPackageOffered());
        }

        if (request.getTechStack() != null) {
            drive.setTechStack(request.getTechStack());
        }

        if (request.getOpenings() != null) {
            drive.setOpenings(request.getOpenings());
        }

        if (request.getJobDescription() != null) {
            drive.setJobDescription(request.getJobDescription());
        }

        if (request.getHasBond() != null) {
            drive.setHasBond(request.getHasBond());
        }

        if (request.getBondDuration() != null) {
            drive.setBondDuration(request.getBondDuration());
        }

        if (request.getMinimumCgpa() != null) {
            drive.setMinimumCgpa(request.getMinimumCgpa());
        }

        if (request.getApplicationDeadline() != null) {
            drive.setApplicationDeadline(request.getApplicationDeadline());
        }

        if (request.getDriveDate() != null) {
            drive.setDriveDate(request.getDriveDate());
        }

        if (request.getIsActive() != null) {
            drive.setIsActive(request.getIsActive());
        }

        driveRepository.save(drive);

        // Create notification about drive update
        notificationService.createBroadcastNotification(
                "Placement Drive Updated",
                "The placement drive " + drive.getTitle() + " has been updated. Please check the details.");
    }
}