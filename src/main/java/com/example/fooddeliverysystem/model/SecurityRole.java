package com.example.fooddeliverysystem.model;

import com.example.fooddeliverysystem.model.Role;
import org.springframework.security.core.GrantedAuthority;

public class SecurityRole implements GrantedAuthority {

    private final Role role;

    public SecurityRole(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName();
    }

    @Override
    public String toString() {
        return "SecurityRole{" +
                "role=" + role +
                '}';
    }
}
