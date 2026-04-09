package com.skillvault.dto;

public class JwtResponse {

    private String token;
    private Long id;
    private String username;
    private String email;
    private String role;

    // ✅ DEFAULT CONSTRUCTOR (VERY IMPORTANT)
    public JwtResponse() {
    }

    // ✅ PARAM CONSTRUCTOR
    public JwtResponse(String token, Long id, String username, String email, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // ✅ GETTERS (MANDATORY)
    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // ✅ SETTERS
    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}