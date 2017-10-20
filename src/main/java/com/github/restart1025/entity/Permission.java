package com.github.restart1025.entity;

import java.util.List;

public class Permission extends BaseEntity {

    private String permissionSn;
    private String permissionName;
    private List<Role> roles;

    public String getPermissionSn() {
        return permissionSn;
    }

    public void setPermissionSn(String permissionSn) {
        this.permissionSn = permissionSn;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
