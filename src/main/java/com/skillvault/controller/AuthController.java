package com.skillvault.controller;

import com.skillvault.dto.JwtResponse;
import com.skillvault.dto.LoginRequest;
import com.skillvault.dto.MessageResponse;
import com.skillvault.dto.RegisterRequest;
import com.skillvault.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://skillvault-frontend1-production.up.railway.app", maxAge = 3600)

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterRequest signUpRequest) {
        try {
            MessageResponse response = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
