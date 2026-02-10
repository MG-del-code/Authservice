package com.authcentral.authservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.Role;
import com.authcentral.authservice.domain.User;
import com.authcentral.authservice.domain.UserApplicationRole;
import com.authcentral.authservice.dto.ReplaceRoleRequest;
import com.authcentral.authservice.repository.ClientApplicationRepository;
import com.authcentral.authservice.repository.RoleRepository;
import com.authcentral.authservice.repository.UserApplicationRoleRepository;
import com.authcentral.authservice.repository.UserRepository;

@Service
public class ReplaceRoleService {

    private final UserRepository userRepository;
    private final ClientApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final UserApplicationRoleRepository userApplicationRoleRepository;

    public ReplaceRoleService(
            UserRepository userRepository,
            ClientApplicationRepository applicationRepository,
            RoleRepository roleRepository,
            UserApplicationRoleRepository userApplicationRoleRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.roleRepository = roleRepository;
        this.userApplicationRoleRepository = userApplicationRoleRepository;
    }

    @Transactional
    public void replaceRole(ReplaceRoleRequest request) {

        /* 1️⃣ Application cliente (via JWT) */
        String clientId = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        ClientApplication application = applicationRepository
                .findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Application inconnue"));

        /* 2️⃣ Utilisateur */
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        /* 3️⃣ Rôles */
        Role oldRole = roleRepository
                .findByNameAndApplication(request.getOldRole(), application)
                .orElseThrow(() -> new IllegalArgumentException("Ancien rôle inexistant"));

        Role newRole = roleRepository
                .findByNameAndApplication(request.getNewRole(), application)
                .orElseThrow(() -> new IllegalArgumentException("Nouveau rôle inexistant"));

        /* 4️⃣ Vérifier que l'utilisateur possède l'ancien rôle */
        UserApplicationRole existingLink = userApplicationRoleRepository
                .findByUserAndApplicationAndRole(user, application, oldRole)
                .orElseThrow(() -> new IllegalStateException(
                        "L'utilisateur ne possède pas le rôle à remplacer"));

        /* 5️⃣ Interdire doublon */
        if (userApplicationRoleRepository
                .existsByUserAndApplicationAndRole(user, application, newRole)) {
            throw new IllegalStateException("L'utilisateur possède déjà le nouveau rôle");
        }

        /* 6️⃣ Remplacement atomique */
        userApplicationRoleRepository.delete(existingLink);

        UserApplicationRole newLink =
                new UserApplicationRole(user, application, newRole);

        userApplicationRoleRepository.save(newLink);
    }
}
