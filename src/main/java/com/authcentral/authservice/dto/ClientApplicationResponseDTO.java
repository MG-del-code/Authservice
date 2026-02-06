package com.authcentral.authservice.dto;

public record ClientApplicationResponseDTO(
        Long id,
        String name,
        String clientId,
        String clientSecret
) {}
