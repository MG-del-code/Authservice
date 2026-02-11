package com.authcentral.authservice.dto;

import java.util.List;

public class RoleWithPermissionsResponse {

    private String name;
    private List<String> permissions;

    public RoleWithPermissionsResponse() {
    }

    public RoleWithPermissionsResponse(String name, List<String> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

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
