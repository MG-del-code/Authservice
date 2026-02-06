package com.authcentral.authservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import com.authcentral.authservice.domain.*;
import com.authcentral.authservice.dto.RegisterRequest;
import com.authcentral.authservice.dto.RegisterResponse;
import com.authcentral.authservice.repository.*;
 

@Service 
public class RegistrationService {

    private final UserRepository userRepository;
    private final ClientApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final UserApplicationRoleRepository userApplicationRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(ClientApplicationRepository applicationRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserApplicationRoleRepository userApplicationRoleRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userApplicationRoleRepository = userApplicationRoleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        // 1. Vérifier l’application cliente
        ClientApplication application = applicationRepository
                .findByClientId(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Application inconnue"));

        // 2. Créer ou récupérer l’utilisateur
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User u = new User(
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword())
                    );
                    return userRepository.save(u);
                });

        // 3. Vérifier s’il est déjà inscrit dans cette application
        if (userApplicationRoleRepository.existsByUserAndApplication(user, application)) {
            throw new IllegalStateException("Utilisateur déjà inscrit dans cette application");
        }

        // 4. Rôle par défaut de l’application
        Role defaultRole = roleRepository
                .findByNameAndApplication("USER", application)
                .orElseThrow(() -> new IllegalStateException("Rôle USER manquant"));

        // 5. Lier user ↔ application ↔ rôle
        UserApplicationRole link = new UserApplicationRole(user, application, defaultRole);
 
        userApplicationRoleRepository.save(link);

        // 6. Réponse
        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setApplicationName(application.getName());
        response.setRole(defaultRole.getName());

        return response;
    }
}
