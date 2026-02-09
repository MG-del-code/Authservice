package com.authcentral.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.authcentral.authservice.dto.SecurityBootstrapRequest;
import com.authcentral.authservice.service.SecurityBootstrapService;

@RestController
@RequestMapping("/api/applications/security")
public class ApplicationSecurityController {

    private final SecurityBootstrapService securityBootstrapService;

    public ApplicationSecurityController(SecurityBootstrapService securityBootstrapService) {
        this.securityBootstrapService = securityBootstrapService;
    }

    @PostMapping("/bootstrap")
    public ResponseEntity<Void> bootstrap(
            @RequestBody SecurityBootstrapRequest request,
            Authentication authentication) {

        String clientId = authentication.getName(); // <-- vient du JWT
        securityBootstrapService.bootstrap(clientId, request);

        return ResponseEntity.ok().build();
    }
}
