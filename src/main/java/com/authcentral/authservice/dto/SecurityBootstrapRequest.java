package com.authcentral.authservice.dto;

import java.util.List;

public class SecurityBootstrapRequest {

    private List<RoleRequest> roles;

    public List<RoleRequest> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleRequest> roles) {
        this.roles = roles;
    }

    public static class RoleRequest {
        private String name;
        private List<String> permissions;

        public String getName() {
            return name;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
    }
}
