package com.bidomi.astrid.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails extends User implements UserDetails {

    public CustomUserDetails(final User byUsername) {
        super(byUsername);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getName() { return super.getName(); }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public Long getRegistrationDate() { return super.getRegistrationDate(); }

    @Override
    public Long getLastVisit() {
        return super.getLastVisit();
    }

    @Override
    public String getConfirmationToken() {
        return super.getConfirmationToken();
    }

    @Override
    public String getPhoneNumber() { return super.getPhoneNumber(); }

    @Override
    public void setBasePoint(BasePoint basePoint) { super.setBasePoint(basePoint); }
}
