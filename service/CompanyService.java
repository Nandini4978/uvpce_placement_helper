package com.example.uvpce_placement_helper.service;

import com.example.uvpce_placement_helper.dto.CompanyDto;
import com.example.uvpce_placement_helper.dto.PlacementDriveDto;
import com.example.uvpce_placement_helper.entity.Company;
import com.example.uvpce_placement_helper.entity.PlacementDrive;
import com.example.uvpce_placement_helper.entity.Student;
import com.example.uvpce_placement_helper.repository.CompanyRepository;
import com.example.uvpce_placement_helper.repository.PlacementDriveRepository;
import com.example.uvpce_placement_helper.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PlacementDriveRepository driveRepository;

    @Autowired
    private StudentRepository studentRepository;

    public CompanyDto.CompanyResponse getCompanyProfile(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return new CompanyDto.CompanyResponse(
                company.getId(),
                company.getEmail(),
                company.getName(),
                company.getLocation(),
                company.getWebsite(),
                company.getIndustry(),
                company.getDescription(),
                company.isApproved()
        );
    }

    public CompanyDto.CompanyResponse updateCompanyProfile(Long companyId, CompanyDto.CompanyProfileUpdateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (request.getName() != null) {
            company.setName(request.getName());
        }

        if (request.getLocation() != null) {
            company.setLocation(request.getLocation());
        }

        if (request.getWebsite() != null) {
            company.setWebsite(request.getWebsite());
        }

        if (request.getIndustry() != null) {
            company.setIndustry(request.getIndustry());
        }

        if (request.getDescription() != null) {
            company.setDescription(request.getDescription());
        }

        companyRepository.save(company);

        return new CompanyDto.CompanyResponse(
                company.getId(),
                company.getEmail(),
                company.getName(),
                company.getLocation(),
                company.getWebsite(),
                company.getIndustry(),
                company.getDescription(),
                company.isApproved()
        );
    }

    public List<PlacementDriveDto.PlacementDriveResponse> getCompanyDrives(Long companyId) {
        List<PlacementDrive> drives = driveRepository.findByCompanyId(companyId);

        return drives.stream()
                .map(this::mapToDriveResponse)
                .collect(Collectors.toList());
    }

    public List<Student> getDriveApplicants(Long driveId, Long companyId) {
        // First verify the drive belongs to this company
        PlacementDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Placement drive not found"));

        if (!drive.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("This drive does not belong to your company");
        }

        return studentRepository.findStudentsByDriveId(driveId);
    }

    private PlacementDriveDto.PlacementDriveResponse mapToDriveResponse(PlacementDrive drive) {
        return new PlacementDriveDto.PlacementDriveResponse(
                drive.getId(),
                drive.getTitle(),
                drive.getCompany().getId(),
                drive.getCompany().getName(),
                drive.getLocation(),
                drive.getPackageOffered(),
                drive.getTechStack(),
                drive.getOpenings(),
                drive.getJobDescription(),
                drive.getHasBond(),
                drive.getBondDuration(),
                drive.getMinimumCgpa(),
                drive.getApplicationDeadline(),
                drive.getDriveDate(),
                drive.getIsActive(),
                drive.getApplicants().size()
        );
    }
}