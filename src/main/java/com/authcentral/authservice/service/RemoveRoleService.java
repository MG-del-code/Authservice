package com.authcentral.authservice.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.Role;
import com.authcentral.authservice.domain.User;
import com.authcentral.authservice.domain.UserApplicationRole;
import com.authcentral.authservice.repository.ClientApplicationRepository;
import com.authcentral.authservice.repository.RoleRepository;
import com.authcentral.authservice.repository.UserApplicationRoleRepository;
import com.authcentral.authservice.repository.UserRepository;

@Service
public class RemoveRoleService {

    private final UserRepository userRepository;
    private final ClientApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final UserApplicationRoleRepository userApplicationRoleRepository;

    public RemoveRoleService(
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

    private ClientApplication getCurrentApplication() {
        String clientId = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return applicationRepository
                .findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Application inconnue"));
    }

    @Transactional
    public void removeRole(String email, String roleName) {

        ClientApplication application = getCurrentApplication();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        Role role = roleRepository
                .findByNameAndApplication(roleName, application)
                .orElseThrow(() -> new IllegalArgumentException("Rôle introuvable"));

        List<UserApplicationRole> roles =
                userApplicationRoleRepository.findByUserAndApplication(user, application);

        if (roles.size() <= 1) {
            throw new IllegalStateException(
                    "Impossible de supprimer le dernier rôle de l'utilisateur"
            );
        }

        UserApplicationRole link =
                userApplicationRoleRepository
                        .findByUserAndApplicationAndRole(user, application, role)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Rôle non attribué à l'utilisateur")
                        );

        userApplicationRoleRepository.delete(link);
    }
}
