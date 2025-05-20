package com.chamz.tryonbackend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String role; // e.g., "CUSTOMER", "DESIGNER"
}
