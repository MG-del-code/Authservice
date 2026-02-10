package com.authcentral.authservice.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authcentral.authservice.dto.RemoveRoleRequest;
import com.authcentral.authservice.dto.ReplaceRoleRequest;
import com.authcentral.authservice.service.RemoveRoleService;
import com.authcentral.authservice.service.ReplaceRoleService;

@RestController
@RequestMapping("/api/users/roles")
public class UserRoleManagementController {

    private final RemoveRoleService removeRoleService;
    private final ReplaceRoleService replaceRoleService;

    public UserRoleManagementController(
            RemoveRoleService removeRoleService,
            ReplaceRoleService replaceRoleService
    ) {
        this.removeRoleService = removeRoleService;
        this.replaceRoleService = replaceRoleService;
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeRole(
            @Valid @RequestBody RemoveRoleRequest request
    ) {
        removeRoleService.removeRole(
                request.getEmail(),
                request.getRoleName()
        );
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/replace")
    public ResponseEntity<Void> replaceRole(
            @Valid @RequestBody ReplaceRoleRequest request
    ) {
        replaceRoleService.replaceRole(request);
        return ResponseEntity.noContent().build();
    }
}
