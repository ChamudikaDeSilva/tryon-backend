package com.chamz.tryonbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse 
{
    private String token;
    private String email;
    private String username;
    private String role;
    private String refreshToken;

    private String message;
}
