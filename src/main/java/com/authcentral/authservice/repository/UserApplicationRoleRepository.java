package com.authcentral.authservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.User;
import com.authcentral.authservice.domain.Role;
import com.authcentral.authservice.domain.UserApplicationRole;

public interface UserApplicationRoleRepository extends JpaRepository<UserApplicationRole, Long> {

    List<UserApplicationRole> findByUserAndApplication(User user, ClientApplication application);

    Optional<UserApplicationRole> findByUserAndApplicationAndRole(
            User user,
            ClientApplication application,
            Role role
    );

    boolean existsByUserAndApplication(User user, ClientApplication application);

    boolean existsByUserAndApplicationAndRole(
        User user,
        ClientApplication application,
        Role role
    );
}
 