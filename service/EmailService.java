package com.example.uvpce_placement_helper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendOtp(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Your OTP for Placement Helper");
        message.setText("Your OTP is: " + otp + "\nIt will expire in 5 minutes.");
        mailSender.send(message);
    }

    public void sendSelectionEmail(String to, String companyName, String driveName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("Congratulations! You've been shortlisted");
        message.setText("Congratulations! You have been shortlisted by " + companyName
                + " for the " + driveName + " drive. Please check your dashboard for more details.");
        mailSender.send(message);
    }

    public void sendAccountApprovalEmail(String to, boolean isApproved) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);

        if (isApproved) {
            message.setSubject("Your Account has been Approved");
            message.setText("Your account has been approved. You can now log in to the Placement Helper platform.");
        } else {
            message.setSubject("Your Account has been Rejected");
            message.setText("Your account registration has been rejected. Please contact the administrator for more information.");
        }

        mailSender.send(message);
    }

    public String generateOtp() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
}