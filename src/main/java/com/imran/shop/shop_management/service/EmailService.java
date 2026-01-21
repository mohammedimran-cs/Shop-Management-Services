package com.imran.shop.shop_management.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("Shop Management <a08098001@smtp-brevo.com>");
            helper.setTo(to);
            helper.setSubject("Verify your Shop Management Account");

            String html = """
            <div style="font-family: Arial; padding:20px">
                <h2>Welcome to Shop Management</h2>
                <p>Thank you for creating an account.</p>
                <p>Please click the button below to verify your email:</p>
                <a href="%s"
                   style="display:inline-block;padding:12px 20px;
                          background:#4CAF50;color:white;
                          text-decoration:none;border-radius:5px;">
                   Verify Email
                </a>
                <p>If you did not sign up, ignore this email.</p>
                <p>This link expires in 30 minutes.</p>
            </div>
            """.formatted(link);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Email sending failed");
        }
    }
    public void sendPasswordResetEmail(String to, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("Shop Management <a08098001@smtp-brevo.com>");
            helper.setTo(to);
            helper.setSubject("Reset your password");

            String html = """
              <h2>Password Reset</h2>
              <p>Click the button to reset your password:</p>
              <a href="%s" style="padding:10px;background:#4CAF50;color:white">
                 Reset Password
              </a>
              <p>This link expires in 30 minutes.</p>
            """.formatted(link);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Email sending failed");
        }
    }

}


