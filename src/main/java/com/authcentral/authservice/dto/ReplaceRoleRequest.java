package com.authcentral.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ReplaceRoleRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String oldRole;

    @NotBlank
    private String newRole;

        public String getEmail() {
        return email;
    }

    public String getOldRole() {
        return oldRole;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOldRole(String oldRole) {
        this.oldRole = oldRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}
