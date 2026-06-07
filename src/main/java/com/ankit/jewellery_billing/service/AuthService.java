package com.ankit.jewellery_billing.service;

import com.ankit.jewellery_billing.dto.LoginRequest;
import com.ankit.jewellery_billing.dto.LoginResponse;
import com.ankit.jewellery_billing.dto.RegisterRequest;
import com.ankit.jewellery_billing.dto.GoogleLoginRequest;
import com.ankit.jewellery_billing.entity.User;
import com.ankit.jewellery_billing.repository.UserRepository;
import com.ankit.jewellery_billing.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client.id}")
    private String googleClientId;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenProvider.generateToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ADMIN")
                .enabled(true)
                .build();

        userRepository.save(user);

        // Auto-login the user after registration
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtTokenProvider.generateToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .message("User registered and logged in successfully")
                .build();
    }

    public LoginResponse googleLogin(GoogleLoginRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();

                // If user doesn't exist, register them
                User user = userRepository.findByUsername(email)
                        .orElseGet(() -> {
                            User newUser = User.builder()
                                    .username(email)
                                    .password(passwordEncoder.encode("google-oauth-dummy-pass-" + UUID.randomUUID()))
                                    .role("ADMIN")
                                    .enabled(true)
                                    .build();
                            return userRepository.save(newUser);
                        });

                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                String token = jwtTokenProvider.generateToken(userDetails);

                return LoginResponse.builder()
                        .token(token)
                        .username(user.getUsername())
                        .role(user.getRole())
                        .message("Google login successful")
                        .build();
            } else {
                throw new RuntimeException("Invalid Google ID Token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error verifying Google ID Token: " + e.getMessage(), e);
        }
    }
}
