package com.authcentral.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authcentral.authservice.dto.AssignRoleRequest;
import com.authcentral.authservice.service.RoleAssignmentService;

@RestController
@RequestMapping("/api/users/roles")
public class RoleAssignmentController {

    private final RoleAssignmentService roleAssignmentService;

    public RoleAssignmentController(RoleAssignmentService roleAssignmentService) {
        this.roleAssignmentService = roleAssignmentService;
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignRole(
            @Validated @RequestBody AssignRoleRequest request) {

        roleAssignmentService.assignRole(request);
        return ResponseEntity.noContent().build();
    }
}

