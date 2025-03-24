package com.mobile.api.security.custom;

import com.mobile.api.model.entity.Permission;
import com.mobile.api.model.entity.User;
import com.mobile.api.security.jwt.JwtClaimsUtil;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final User user;
    @Getter
    private final List<String> pcodeList;

    public CustomUserDetails(User user) {
        this.user = user;
        this.pcodeList = user.getAccount().getGroup().getPermissions().stream()
                .map(Permission::getCode)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return pcodeList.stream()
                .map(pcode -> (GrantedAuthority) () -> pcode)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getClaims() {
        return JwtClaimsUtil.extractClaims(this);
    }

    public Long getId() {
        return user.getAccount().getId();
    }

    public String getEmail() {
        return user.getAccount().getEmail();
    }

    public Boolean getIsSuperAdmin() {
        return user.getAccount().getIsSuperAdmin();
    }

    @Override
    public String getPassword() {
        return user.getAccount().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getAccount().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
