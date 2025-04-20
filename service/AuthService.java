package com.example.uvpce_placement_helper.service;

import com.example.uvpce_placement_helper.dto.AuthDto;
import com.example.uvpce_placement_helper.entity.Admin;
import com.example.uvpce_placement_helper.entity.Company;
import com.example.uvpce_placement_helper.entity.Student;
import com.example.uvpce_placement_helper.entity.User;
import com.example.uvpce_placement_helper.repository.AdminRepository;
import com.example.uvpce_placement_helper.repository.CompanyRepository;
import com.example.uvpce_placement_helper.repository.StudentRepository;
import com.example.uvpce_placement_helper.repository.UserRepository;
import com.example.uvpce_placement_helper.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public String generateOtp(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String otp = emailService.generateOtp();
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);
            emailService.sendOtp(email, otp);
            return "OTP sent to your email";
        }
        return "User with email " + email + " not found";
    }

    public AuthDto.JwtResponse verifyOtp(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getOtp() != null && user.getOtp().equals(otp) &&
                    user.getOtpExpiry() != null && LocalDateTime.now().isBefore(user.getOtpExpiry())) {

                // Clear OTP after successful verification
                user.setOtp(null);
                user.setOtpExpiry(null);
                userRepository.save(user);

                // Generate JWT token
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String role = user.getRoles().iterator().next(); // Get first role
                String token = jwtUtil.generateToken(userDetails, role, user.getId());

                return new AuthDto.JwtResponse(token, role, user.getId(), email, user.isApproved());
            }
        }

        throw new RuntimeException("Invalid OTP or OTP expired");
    }

    public boolean registerStudent(Student student, String confirmPassword) {
        if (!student.getPassword().equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (studentRepository.existsByEnrollmentNumber(student.getEnrollmentNumber())) {
            throw new RuntimeException("Enrollment number already registered");
        }

        // Set student role and encode password
        student.setRoles(Collections.singleton("STUDENT"));
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepository.save(student);

        // Send OTP for verification
        String otp = emailService.generateOtp();
        student.setOtp(otp);
        student.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        studentRepository.save(student);
        emailService.sendOtp(student.getEmail(), otp);

        return true;
    }

    public boolean registerAdmin(Admin admin, String confirmPassword) {
        if (!admin.getPassword().equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Validate admin email (must be college email)
        if (!admin.getEmail().endsWith("@college.edu")) { // Change this to match your college domain
            throw new RuntimeException("Admin must use a college email address");
        }

        // Set admin role and encode password
        admin.setRoles(Collections.singleton("ADMIN"));
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setApproved(true); // Auto-approve admins with college email
        adminRepository.save(admin);

        // Send OTP for verification
        String otp = emailService.generateOtp();
        admin.setOtp(otp);
        admin.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        adminRepository.save(admin);
        emailService.sendOtp(admin.getEmail(), otp);

        return true;
    }

    public boolean registerCompany(Company company, String confirmPassword) {
        if (!company.getPassword().equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByEmail(company.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Set company role and encode password
        company.setRoles(Collections.singleton("COMPANY"));
        company.setPassword(passwordEncoder.encode(company.getPassword()));
        companyRepository.save(company);

        // Send OTP for verification
        String otp = emailService.generateOtp();
        company.setOtp(otp);
        company.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        companyRepository.save(company);
        emailService.sendOtp(company.getEmail(), otp);

        return true;
    }
}