package com.authcentral.authservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authcentral.authservice.domain.Role;
import com.authcentral.authservice.domain.Permission;
import com.authcentral.authservice.domain.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole(Role role);

    boolean existsByRoleAndPermission(Role role, Permission permission);

    long countByRole(Role role);

    void deleteByRoleAndPermission(Role role, Permission permission);
}
