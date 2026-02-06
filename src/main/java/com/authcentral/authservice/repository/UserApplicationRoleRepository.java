package com.authcentral.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.User;
import com.authcentral.authservice.domain.UserApplicationRole;

public interface UserApplicationRoleRepository extends JpaRepository<UserApplicationRole, Long> {

    Optional<UserApplicationRole> findByUserAndApplication(User user, ClientApplication application);

    boolean existsByUserAndApplication(User user, ClientApplication application);
}
