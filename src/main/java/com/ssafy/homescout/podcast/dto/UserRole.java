package com.ssafy.homescout.podcast.dto;

public enum UserRole {
    NORMAL("NORMAL"),
    REALTOR("REALTOR");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static UserRole fromString(String roleName) {
        for (UserRole role : UserRole.values()) {
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant for role name: " + roleName);
    }
}
