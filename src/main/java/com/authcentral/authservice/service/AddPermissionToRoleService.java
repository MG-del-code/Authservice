package com.authcentral.authservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authcentral.authservice.domain.*;
import com.authcentral.authservice.dto.AddPermissionRequest;
import com.authcentral.authservice.repository.*;

@Service
public class AddPermissionToRoleService {

    private final ClientApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public AddPermissionToRoleService(
            ClientApplicationRepository applicationRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository) {

        this.applicationRepository = applicationRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Transactional
    public void add(AddPermissionRequest request) {

        String clientId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        ClientApplication app = applicationRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalStateException("Application inconnue"));

        Role role = roleRepository.findByNameAndApplication(request.getRoleName(), app)
                .orElseThrow(() -> new IllegalStateException("Rôle inexistant"));

        Permission permission = permissionRepository
                .findByNameAndApplication(request.getPermissionName(), app)
                .orElseGet(() -> permissionRepository.save(
                        new Permission(request.getPermissionName(), app)
                ));

        if (!rolePermissionRepository.existsByRoleAndPermission(role, permission)) {
            rolePermissionRepository.save(new RolePermission(role, permission));
        }
    }
}
