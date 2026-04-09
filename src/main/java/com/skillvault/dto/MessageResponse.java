package com.skillvault.dto;

public class MessageResponse {

    private String message;

    // ✅ Default constructor (VERY IMPORTANT)
    public MessageResponse() {
    }

    // ✅ Parameterized constructor
    public MessageResponse(String message) {
        this.message = message;
    }

    // ✅ Getter (MANDATORY)
    public String getMessage() {
        return message;
    }

    // ✅ Setter
    public void setMessage(String message) {
        this.message = message;
    }
}