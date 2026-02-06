package com.authcentral.authservice.controller;

import com.authcentral.authservice.dto.ClientApplicationRequestDTO;
import com.authcentral.authservice.dto.ClientApplicationResponseDTO;
import com.authcentral.authservice.domain.ClientApplication;
import com.authcentral.authservice.service.ClientApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientApplicationController {

    private final ClientApplicationService service;

    public ClientApplicationController(ClientApplicationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientApplicationResponseDTO> registerApplication(
            @RequestBody @Valid ClientApplicationRequestDTO request) {

        ClientApplication app = service.registerApplication(request.name());

        // On renvoie le clientSecret uniquement à la création
        ClientApplicationResponseDTO response = new ClientApplicationResponseDTO(
                app.getId(),
                app.getName(),
                app.getClientId(),
                app.getClientSecret()
        );

        // return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
