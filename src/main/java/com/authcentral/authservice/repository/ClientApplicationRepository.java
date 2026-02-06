package com.authcentral.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authcentral.authservice.domain.ClientApplication;

public interface ClientApplicationRepository extends JpaRepository<ClientApplication, Long> {

    Optional<ClientApplication> findByClientId(String clientId);

    boolean existsByClientId(String clientId);
}
