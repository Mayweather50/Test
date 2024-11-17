package com.example.demo.security;

import com.example.demo.dto.auth.LoginRequestDTO;
import com.example.demo.dto.auth.LoginResponseDTO;
import com.example.demo.dto.auth.RegisterRequestDTO;
import com.example.demo.entity.User;
import com.example.demo.enums.*;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterRequestDTO request) {
        try {
            ValidationUtils.validateEmail(request.getEmail());
            ValidationUtils.validatePassword(request.getPassword());

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ValidationException("Email already registered");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.ROLE_USER);

            userRepository.save(user);

            log.info("Successfully registered new user with email: {}", request.getEmail());

        } catch (ValidationException e) {
            log.error("Registration validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during registration: {}", e.getMessage());
            throw new RuntimeException("Registration failed", e);
        }
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            ValidationUtils.validateEmail(request.getEmail());
            ValidationUtils.validatePassword(request.getPassword());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new AuthenticationException("User not found"));

            var authorities = Collections.singleton(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );

            var userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );

            var token = jwtService.generateToken(userDetails);

            log.info("Successfully authenticated user: {}", request.getEmail());

            return LoginResponseDTO.builder()
                    .token(token)
                    .build();

        } catch (ValidationException e) {
            log.error("Login validation failed: {}", e.getMessage());
            throw new AuthenticationException("Invalid credentials");
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user {}: {}", request.getEmail(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage());
            throw new AuthenticationException("Authentication failed");
        }
    }
}