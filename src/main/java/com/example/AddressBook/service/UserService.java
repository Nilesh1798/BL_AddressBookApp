package com.example.AddressBook.service;

import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.LoginResponseDTO;
import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository authUserRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public String registerUser(UserDTO userDTO) {
        if (authUserRepository.findByEmail(userDTO.getEmail()).isPresent()) { // ✅ Fixed repository usage
            return "Email is already in use.";
        }

        AuthUser user = AuthUser.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .role(userDTO.getRole())
                .build();

        authUserRepository.save(user);

        try {
            emailService.sendEmail(user.getEmail(), "Welcome!", "Your registration is successful.");
        } catch (Exception e) {
            return "User registered, but email notification failed.";
        }

        return "User registered successfully!";
    }

    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(loginDTO.getEmail()); // ✅ Fixed repository usage

        if (userOptional.isEmpty()) {
            return new LoginResponseDTO("User not found!", null);
        }

        AuthUser user = userOptional.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return new LoginResponseDTO("Invalid email or password!", null);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponseDTO("Login successful!", token);
    }

    public String forgotPassword(String email, String newPassword) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(email); // ✅ Fixed repository usage

        if (userOptional.isEmpty()) {
            return "Sorry! We cannot find the user email: " + email;
        }

        AuthUser user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(user);

        try {
            String subject = "Password Reset Confirmation";
            String message = "Hello " + user.getFirstName() + ", Your password has been successfully updated.";
            emailService.sendEmail(user.getEmail(), subject, message);
        } catch (Exception e) {
            return "Password updated, but email notification failed.";
        }

        return "Password has been changed successfully!";
    }

    public String resetPassword(String email, String currentPassword, String newPassword) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(email); // ✅ Fixed repository usage

        if (userOptional.isEmpty()) {
            return "User not found with email: " + email;
        }

        AuthUser user = userOptional.get();

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
            return jwtUtil.validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }
}
