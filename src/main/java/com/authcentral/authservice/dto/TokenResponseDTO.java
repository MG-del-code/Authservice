package com.authcentral.authservice.dto;

public record TokenResponseDTO(
        String accessToken,
        String tokenType,
        long expiresIn
) {}
