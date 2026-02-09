package com.authcentral.authservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public RegistrationService(
        ClientApplicationRepository applicationRepository,
        PasswordEncoder passwordEncoder,
        RoleRepository roleRepository,
        UserApplicationRoleRepository userApplicationRoleRepository,
        UserRepository userRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userApplicationRoleRepository = userApplicationRoleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        /* ==========================
           1. Récupération de l’app cliente depuis le JWT
           ========================== */
        String clientId = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        ClientApplication application = applicationRepository
                .findByClientId(clientId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Application cliente inconnue")
                );

        /* ==========================
           2. Création ou récupération de l’utilisateur global
           ========================== */
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User u = new User(
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword())
                    );
                    return userRepository.save(u);
                });

        /* ==========================
           3. Création ou récupération du rôle USER (auto-bootstrap)
           ========================== */
        Role defaultRole = roleRepository
                .findByNameAndApplication("USER", application)
                .orElseGet(() -> {
                    Role role = new Role("USER", application);
                    return roleRepository.save(role);
                });

        /* ==========================
           4. Vérifier doublon USER ↔ APPLICATION ↔ ROLE
           ========================== */
        boolean alreadyLinked =
                userApplicationRoleRepository
                    .existsByUserAndApplicationAndRole(
                        user,
                        application,
                        defaultRole
                    );

        if (alreadyLinked) {
            throw new IllegalStateException(
                "L'utilisateur possède déjà ce rôle dans cette application"
            );
        }

        /* ==========================
           5. Lier User ↔ Application ↔ Role
           ========================== */
        UserApplicationRole link =
                new UserApplicationRole(user, application, defaultRole);

        userApplicationRoleRepository.save(link);

        /* ==========================
           6. Réponse
           ========================== */
        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setApplicationName(application.getName());
        response.setRole(defaultRole.getName());

        return response;
    }
}
