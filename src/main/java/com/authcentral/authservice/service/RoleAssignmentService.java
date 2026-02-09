package com.authcentral.authservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.Role;
import com.authcentral.authservice.domain.User;
import com.authcentral.authservice.domain.UserApplicationRole;
import com.authcentral.authservice.dto.AssignRoleRequest;
import com.authcentral.authservice.repository.ClientApplicationRepository;
import com.authcentral.authservice.repository.RoleRepository;
import com.authcentral.authservice.repository.UserApplicationRoleRepository;
import com.authcentral.authservice.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleAssignmentService {

    private final UserRepository userRepository;
    private final ClientApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final UserApplicationRoleRepository userApplicationRoleRepository;

    public RoleAssignmentService(
        UserRepository userRepository,
        ClientApplicationRepository applicationRepository,
        RoleRepository roleRepository,
        UserApplicationRoleRepository userApplicationRoleRepository
    ) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.roleRepository = roleRepository;
        this.userApplicationRoleRepository = userApplicationRoleRepository;
    }

    public void assignRole(AssignRoleRequest request) {

        /* ==========================
           1. App cliente depuis le JWT
           ========================== */
        String clientId = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        ClientApplication application = applicationRepository
                .findByClientId(clientId)
                .orElseThrow(() ->
                    new IllegalArgumentException("Application inconnue")
                );

        /* ==========================
           2. Utilisateur
           ========================== */
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                    new IllegalArgumentException("Utilisateur introuvable")
                );

        /* ==========================
           3. Rôle (doit appartenir à l’app)
           ========================== */
        Role role = roleRepository
                .findByNameAndApplication(
                    request.getRoleName(),
                    application
                )
                .orElseThrow(() ->
                    new IllegalArgumentException("Rôle inexistant pour cette application")
                );

        /* ==========================
           4. Éviter les doublons
           ========================== */
        boolean exists =
                userApplicationRoleRepository
                    .existsByUserAndApplicationAndRole(
                        user,
                        application,
                        role
                    );

        if (exists) {
            throw new IllegalStateException(
                "L'utilisateur possède déjà ce rôle"
            );
        }

        /* ==========================
           5. Association
           ========================== */
        UserApplicationRole link =
                new UserApplicationRole(user, application, role);

        userApplicationRoleRepository.save(link);
    }
}

