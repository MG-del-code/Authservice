package com.authcentral.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authcentral.authservice.dto.ClientCredentialsRequestDTO;
import com.authcentral.authservice.dto.TokenResponseDTO;
import com.authcentral.authservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDTO> getToken(
            @RequestBody ClientCredentialsRequestDTO request
    ) {
        TokenResponseDTO response = authService.authenticateClient(request);
                return ResponseEntity.ok(response);
    }
}
