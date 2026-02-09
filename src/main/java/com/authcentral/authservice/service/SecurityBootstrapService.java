package com.authcentral.authservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authcentral.authservice.domain.*;
import com.authcentral.authservice.dto.SecurityBootstrapRequest;
import com.authcentral.authservice.repository.*;

@Service
public class SecurityBootstrapService {

    private final ClientApplicationRepository appRepo;
    private final RoleRepository roleRepo;
    private final PermissionRepository permissionRepo;
    private final RolePermissionRepository rolePermissionRepo;

    public SecurityBootstrapService(
            ClientApplicationRepository appRepo,
            RoleRepository roleRepo,
            PermissionRepository permissionRepo,
            RolePermissionRepository rolePermissionRepo) {

        this.appRepo = appRepo;
        this.roleRepo = roleRepo;
        this.permissionRepo = permissionRepo;
        this.rolePermissionRepo = rolePermissionRepo;
    }

    @Transactional
    public void bootstrap(String clientId, SecurityBootstrapRequest request) {

        ClientApplication app = appRepo.findByClientId(clientId)
            .orElseThrow(() -> new IllegalStateException("Application inconnue"));

        for (var roleReq : request.getRoles()) {

            Role role = roleRepo
                .findByNameAndApplication(roleReq.getName(), app)
                .orElseGet(() -> roleRepo.save(new Role(roleReq.getName(), app)));

            for (String permName : roleReq.getPermissions()) {

                Permission perm = permissionRepo
                    .findByNameAndApplication(permName, app)
                    .orElseGet(() -> permissionRepo.save(new Permission(permName, app)));

                if (!rolePermissionRepo.existsByRoleAndPermission(role, perm)) {
                    rolePermissionRepo.save(new RolePermission(role, perm));
                }
            }
        }
    }
}
