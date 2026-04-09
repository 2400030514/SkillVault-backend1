package com.skillvault.service;

import com.skillvault.dto.JwtResponse;
import com.skillvault.dto.LoginRequest;
import com.skillvault.dto.MessageResponse;
import com.skillvault.dto.RegisterRequest;
import com.skillvault.model.User;
import com.skillvault.repository.UserRepository;
import com.skillvault.security.JwtUtils;
import com.skillvault.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role);
    }

    public MessageResponse registerUser(RegisterRequest signUpRequest) {
        // Because the frontend only asks for username and we enforce email=username:
        String email = signUpRequest.getUsername();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Error: Email/Username is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setEmail(email);
        user.setName(email); // defaulting name to email as frontend doesn't supply it
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        // Set role based on hardcoded frontend logic if username isAdmin (for dev)
        // Or default to user
        if ("Admin".equalsIgnoreCase(email)) {
             user.setRole("ROLE_ADMIN");
        } else {
             user.setRole("ROLE_USER");
        }

        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
