package com.authcentral.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AssignRoleRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String roleName;

    public String getEmail() {
        return email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
