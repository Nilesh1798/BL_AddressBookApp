package com.example.AddressBook.service;

import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.LoginResponseDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository authUserRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    private UserDTO convertToDTO(AuthUser user) {
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
    }

    public String registerUser(UserDTO userDTO) {
        if (authUserRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return "Email is already in use.";
        }

        AuthUser user = AuthUser.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(userDTO.getRole() != null ? userDTO.getRole() : "USER") // ✅ Default role if not provided
                .build();

        AuthUser savedUser = authUserRepository.save(user);

        // 🔥 Debugging: Ensure user ID is generated
        if (savedUser.getId() == null || savedUser.getId() == 0) {
            throw new RuntimeException("User registration failed: ID not generated.");
        }
        logger.info("User registered successfully: ID = {}", savedUser.getId());

        try {
            emailService.sendEmail(user.getEmail(), "Welcome!", "Your registration is successful.");
        } catch (Exception e) {
            logger.error("Failed to send email to {}", user.getEmail(), e);
            return "User registered, but email notification failed.";
        }

        return "User registered successfully! ID: " + savedUser.getId();
    }

    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(loginDTO.getEmail());

        if (userOptional.isEmpty()) {
            return new LoginResponseDTO("User not found!", null);
        }

        AuthUser user = userOptional.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return new LoginResponseDTO("Invalid email or password!", null);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        logger.info("User logged in: {}", user.getEmail());

        return new LoginResponseDTO("Login successful!", token);
    }

    public String forgotPassword(String email, String newPassword) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(user);

        try {
            String subject = "Password Reset Confirmation";
            String message = "Hello " + user.getFirstName() + ", Your password has been successfully updated.";
            emailService.sendEmail(user.getEmail(), subject, message);
        } catch (Exception e) {
            logger.error("Failed to send password reset email to {}", user.getEmail(), e);
            return "Password updated, but email notification failed.";
        }

        return "Password has been changed successfully!";
    }

    public String resetPassword(String email, String currentPassword, String newPassword) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "Current password is incorrect!";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(user);

        return "Password reset successfully!";
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            if (username == null) return false;

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean isValid = jwtUtil.validateToken(token, userDetails);

            if (isValid) {
                logger.info("Token validated for user: {}", username);
            } else {
                logger.warn("Invalid token for user: {}", username);
            }

            return isValid;
        } catch (Exception e) {
            logger.error("Token validation failed", e);
            return false;
        }
    }

    public UserDTO getUserByEmail(String email) {
        AuthUser user = authUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return convertToDTO(user);
    }
}
