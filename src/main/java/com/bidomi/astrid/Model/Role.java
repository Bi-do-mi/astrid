package com.bidomi.astrid.Model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
