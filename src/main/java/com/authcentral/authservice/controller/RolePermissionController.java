package com.authcentral.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.authcentral.authservice.dto.*;
import com.authcentral.authservice.service.*;

@RestController
@RequestMapping("/api/roles/permissions")
public class RolePermissionController {

    private final AddPermissionToRoleService addService;
    private final RemovePermissionFromRoleService removeService;
    private final ReplacePermissionOfRoleService replaceService;

    public RolePermissionController(
            AddPermissionToRoleService addService,
            RemovePermissionFromRoleService removeService,
            ReplacePermissionOfRoleService replaceService) {

        this.addService = addService;
        this.removeService = removeService;
        this.replaceService = replaceService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> add(@Validated @RequestBody AddPermissionRequest request) {
        addService.add(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> remove(@Validated @RequestBody RemovePermissionRequest request) {
        removeService.remove(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/replace")
    public ResponseEntity<Void> replace(@Validated @RequestBody ReplacePermissionRequest request) {
        replaceService.replace(request);
        return ResponseEntity.ok().build();
    }
}
