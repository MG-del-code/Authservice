package com.authcentral.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ReplacePermissionRequest {

    @NotBlank
    private String roleName;

    @NotBlank
    private String oldPermissionName;

    @NotBlank
    private String newPermissionName;

    public String getRoleName() {
        return roleName;
    }

    public String getOldPermissionName() {
        return oldPermissionName;
    }

    public String getNewPermissionName() {
        return newPermissionName;
    }
}
