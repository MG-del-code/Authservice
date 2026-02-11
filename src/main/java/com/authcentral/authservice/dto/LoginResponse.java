package com.authcentral.authservice.dto;

import java.util.List;

public class LoginResponse {

    private String message;
    private String email;
    private List<RoleWithPermissionsResponse> roles;

    public LoginResponse() {
    }

    public LoginResponse(String message, String email, List<RoleWithPermissionsResponse> roles) {
        this.message = message;
        this.email = email;
        this.roles = roles;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public List<RoleWithPermissionsResponse> getRoles() {
        return roles;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<RoleWithPermissionsResponse> roles) {
        this.roles = roles;
    }
}
