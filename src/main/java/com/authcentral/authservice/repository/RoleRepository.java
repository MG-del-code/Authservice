package com.authcentral.authservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNameAndApplication(String name, ClientApplication application);

    List<Role> findByApplication(ClientApplication application);
}
