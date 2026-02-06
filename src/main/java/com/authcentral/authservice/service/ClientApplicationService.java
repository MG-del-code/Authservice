package com.authcentral.authservice.service;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.repository.ClientApplicationRepository;

@Service
public class ClientApplicationService {

    private final ClientApplicationRepository repository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final PasswordEncoder passwordEncoder;

    public ClientApplicationService(ClientApplicationRepository repository,
                                     PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public ClientApplication registerApplication(String name) {
        String clientId = generateClientId(); 
        String rawClientSecret = generateClientSecret(); // 1. Secret en clair (à retourner au client)
        String hashedClientSecret = passwordEncoder.encode(rawClientSecret); // 2. Secret hashé (à stocker en base)
        ClientApplication app = new ClientApplication(name, clientId, hashedClientSecret); 
        repository.save(app); 
        // IMPORTANT :
        // on remet le secret en clair dans l'objet retourné
        // (pour l'envoyer dans la réponse API)
        app.setClientSecret(rawClientSecret);
        return app;
    }

    private String generateClientId() {
        return "client-" + System.currentTimeMillis(); // simple unique ID
    }

    private String generateClientSecret() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
 