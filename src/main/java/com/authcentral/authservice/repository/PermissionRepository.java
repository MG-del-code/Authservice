package com.authcentral.authservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByNameAndApplication(String name, ClientApplication application);

    List<Permission> findByApplication(ClientApplication application);
}
