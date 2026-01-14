package com.imran.shop.shop_management.controller;

import com.imran.shop.shop_management.DTO.*;
import com.imran.shop.shop_management.entity.Role;
import com.imran.shop.shop_management.entity.User;
import com.imran.shop.shop_management.exception.InvalidTokenException;
import com.imran.shop.shop_management.exception.UserNotFoundException;
import com.imran.shop.shop_management.jwt.JwtUtil;
import com.imran.shop.shop_management.repository.UserRepo;
import com.imran.shop.shop_management.service.EmailService;
import com.imran.shop.shop_management.service.UserDetailsImp;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserDto userDto) {

        if (userRepository.existsByEmail(userDto.email()))
            return ResponseEntity.badRequest().body(
                    new ApiResponse("error", "email already exist")
            );
        User user = new User();
        user.setUserName(userDto.userName());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(false);
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationTokenExpiry(LocalDateTime.now().plusMinutes(30));

        userRepository.save(user);

        String link = "http://localhost:5173/verify?token=" + user.getVerificationToken();
        emailService.sendVerificationEmail(user.getEmail(), link);

        return ResponseEntity.ok(
                new ApiResponse("success",
                        "We have sent a verification email. Please verify and then login.")
        );
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verify(@Valid @RequestParam Token token) {

        User user = (User) userRepository.findByVerificationToken(token.token())
                .orElseThrow(() -> new InvalidTokenException("Link is invalid. Please generate a new one."));

        if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Verification link expired. Please generate a new one.");
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok(
            new ApiResponse("success",
                    "Your email has been verified. Please go to the login page and sign in.")
        );
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse> resend(@Valid @RequestBody EmailRequest email) {

        User user = userRepository.findByEmail(email.email())
                .orElseThrow(() -> new UserNotFoundException("Email is not registered"));

        if(user.isEnabled()){
            throw new UserNotFoundException("This account is already enabled");
        }

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        String link = "http://localhost:5173/verify?token=" + user.getVerificationToken();
        emailService.sendVerificationEmail(user.getEmail(), link);

        return ResponseEntity.ok(
            new ApiResponse("success",
                    "New verification email sent")
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgot(@Valid @RequestBody EmailRequest email) {

        User user = userRepository.findByEmail(email.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setResetToken(UUID.randomUUID().toString());
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        String link = "http://localhost:8080/api/auth/reset?token=" + user.getResetToken();
        emailService.sendPasswordResetEmail(user.getEmail(), link);

        return ResponseEntity.ok(
            new ApiResponse("success",
                    "Password reset link sent to your email")
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@Valid @RequestBody ResetPassword resetPassword) {

        User user = (User) userRepository.findByResetToken(resetPassword.token())
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())){
            throw new InvalidTokenException("Link expired");
        }

        user.setPassword(passwordEncoder.encode(resetPassword.newPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok(
            new ApiResponse("success", "Password changed successfully")
        );
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(),
                                request.password()
                        )
                );

        UserDetailsImp user =
                (UserDetailsImp) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getUsername(),
                "role", user.getAuthorities(),
                "enabled", user.getEnabled()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)   // delete cookie
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("Logged out");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        UserDetailsImp user = (UserDetailsImp) auth.getPrincipal();
        return ResponseEntity.ok(Map.of(
                "email", user.getUsername(),
                "role", user.getAuthorities()
        ));
    }

}

