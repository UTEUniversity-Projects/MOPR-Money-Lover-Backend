package com.mobile.api.security.custom;

import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.model.entity.Account;
import com.mobile.api.model.entity.Permission;
import com.mobile.api.model.entity.User;
import com.mobile.api.repository.jpa.UserRepository;
import com.mobile.api.security.jwt.JwtClaimsUtil;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final String username;
    private final String password;
    private final Boolean isSuperAdmin;
    private final List<String> pcodeList;

    public CustomUserDetails(Long userId, UserRepository userRepository) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Account account = user.getAccount();
        this.id = user.getId();
        this.email = account.getEmail();
        this.username = account.getEmail();
        this.password = account.getPassword();
        this.isSuperAdmin = account.getIsSuperAdmin();
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

    @Override
    public String getPassword() {
        return password;
    }

    public Map<String, Object> getClaims() {
        return JwtClaimsUtil.extractClaims(this);
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
