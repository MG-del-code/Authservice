package com.authcentral.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.dto.ClientCredentialsRequestDTO;
import com.authcentral.authservice.dto.TokenResponseDTO;
import com.authcentral.authservice.repository.ClientApplicationRepository;

@Service
public class AuthService {

    private final ClientApplicationRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(ClientApplicationRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO authenticateClient(ClientCredentialsRequestDTO request) {

        ClientApplication client = repository
                .findByClientId(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client ID invalide"));

        boolean secretMatches = passwordEncoder.matches(
                request.getClientSecret(),
                client.getClientSecret()
        );

        if (!secretMatches) {
            throw new RuntimeException("Client secret invalide"); 
        }
        String token = jwtService.generateToken(client);

        return new TokenResponseDTO(
                token,
                "Bearer",
                15 * 60
        );
    }
}
