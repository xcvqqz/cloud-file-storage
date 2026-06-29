package io.github.xcvqqz.cloud_file_storage.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleName implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
