package com.authcentral.authservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.domain.Role;
import com.authcentral.authservice.repository.ClientApplicationRepository;
import com.authcentral.authservice.repository.RoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            ClientApplicationRepository appRepository,
            RoleRepository roleRepository
    ) {
        return args -> {

            // 1️⃣ Application cliente SYSTEM (obligatoire)
            ClientApplication systemApp = appRepository
                .findByClientId("SYSTEM")
                .orElseGet(() -> {
                    ClientApplication app = new ClientApplication(
                            "System Application",     // name
                            "SYSTEM",                 // clientId
                            "SYSTEM_SECRET_CHANGE_ME" // clientSecret
                    );
                    return appRepository.save(app);
                });

            // 2️⃣ Rôle USER pour SYSTEM
            roleRepository
                .findByNameAndApplication("USER", systemApp)
                .orElseGet(() -> roleRepository.save(
                        new Role("USER", systemApp)
                ));

            // 3️⃣ Rôle ADMIN pour SYSTEM
            roleRepository
                .findByNameAndApplication("ADMIN", systemApp)
                .orElseGet(() -> roleRepository.save(
                        new Role("ADMIN", systemApp)
                ));
        };
    }
}
