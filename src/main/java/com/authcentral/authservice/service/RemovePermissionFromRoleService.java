package com.authcentral.authservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authcentral.authservice.domain.*;
import com.authcentral.authservice.dto.RemovePermissionRequest;
import com.authcentral.authservice.repository.*;

@Service
public class RemovePermissionFromRoleService {

    private final ClientApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RemovePermissionFromRoleService(
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
    public void remove(RemovePermissionRequest request) {

        String clientId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        ClientApplication app = applicationRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalStateException("Application inconnue"));

        Role role = roleRepository.findByNameAndApplication(request.getRoleName(), app)
                .orElseThrow(() -> new IllegalStateException("Rôle inexistant"));

        if (rolePermissionRepository.countByRole(role) <= 1) {
            throw new IllegalStateException("Un rôle doit avoir au moins une permission");
        }

        Permission permission = permissionRepository
                .findByNameAndApplication(request.getPermissionName(), app)
                .orElseThrow(() -> new IllegalStateException("Permission inexistante"));

        if (!rolePermissionRepository.existsByRoleAndPermission(role, permission)) {
                throw new IllegalStateException("La permission n'est pas associée à ce rôle" );
        }

        rolePermissionRepository.deleteByRoleAndPermission(role, permission);
    }
}
