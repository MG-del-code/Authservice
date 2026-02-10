package com.authcentral.authservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authcentral.authservice.domain.*;
import com.authcentral.authservice.dto.ReplacePermissionRequest;
import com.authcentral.authservice.repository.*;

@Service
public class ReplacePermissionOfRoleService {

    private final ClientApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public ReplacePermissionOfRoleService(
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
    public void replace(ReplacePermissionRequest request) {

        String clientId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        ClientApplication app = applicationRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalStateException("Application inconnue"));

        Role role = roleRepository.findByNameAndApplication(request.getRoleName(), app)
                .orElseThrow(() -> new IllegalStateException("Rôle inexistant"));

        Permission oldPermission = permissionRepository
                .findByNameAndApplication(request.getOldPermissionName(), app)
                .orElseThrow(() -> new IllegalStateException("Ancienne permission inexistante"));

        Permission newPermission = permissionRepository
                .findByNameAndApplication(request.getNewPermissionName(), app)
                .orElseGet(() -> permissionRepository.save(
                        new Permission(request.getNewPermissionName(), app)
                ));

        rolePermissionRepository.deleteByRoleAndPermission(role, oldPermission);

        if (!rolePermissionRepository.existsByRoleAndPermission(role, newPermission)) {
            rolePermissionRepository.save(new RolePermission(role, newPermission));
        }
    }
}
