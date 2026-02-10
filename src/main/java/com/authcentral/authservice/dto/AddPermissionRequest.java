package com.authcentral.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class AddPermissionRequest {

    @NotBlank
    private String roleName;

    @NotBlank
    private String permissionName;

    public String getRoleName() {
        return roleName;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
