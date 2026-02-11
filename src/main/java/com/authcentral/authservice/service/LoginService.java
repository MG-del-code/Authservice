package com.authcentral.authservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authcentral.authservice.domain.*;
import com.authcentral.authservice.dto.LoginRequest;
import com.authcentral.authservice.dto.LoginResponse;
import com.authcentral.authservice.dto.RoleWithPermissionsResponse;
import com.authcentral.authservice.repository.*;

@Service
public class LoginService {

    private final ClientApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final UserApplicationRoleRepository userApplicationRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(
            ClientApplicationRepository applicationRepository,
            UserRepository userRepository,
            UserApplicationRoleRepository userApplicationRoleRepository,
            RolePermissionRepository rolePermissionRepository,
            PasswordEncoder passwordEncoder) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.userApplicationRoleRepository = userApplicationRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String clientId = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        ClientApplication app = applicationRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalStateException("Application inconnue"));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }

        List<UserApplicationRole> userRoles =
                userApplicationRoleRepository.findByUserAndApplication(user, app);

        if (userRoles.isEmpty()) {
            throw new IllegalStateException("Cet utilisateur n'appartient pas à cette application");
        }

        List<RoleWithPermissionsResponse> rolesResponse = userRoles.stream()
                .map(link -> {
                    Role role = link.getRole();

                    List<String> permissions = rolePermissionRepository
                            .findByRole(role)
                            .stream()
                            .map(rp -> rp.getPermission().getName())
                            .collect(Collectors.toList());

                    return new RoleWithPermissionsResponse(role.getName(), permissions);
                })
                .collect(Collectors.toList());

        return new LoginResponse(
                "Connexion réussie",
                user.getEmail(),
                rolesResponse
        );
    }
}
