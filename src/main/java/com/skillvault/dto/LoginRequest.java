package com.skillvault.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username; // which is an email in our scenario
    private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
