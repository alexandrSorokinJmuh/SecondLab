package com.example.order_sub_system.security;


import com.example.order_sub_system.dto.CustomerDto;
import com.example.order_sub_system.entities.Role;
import com.example.order_sub_system.entities.UserStatus;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromCustomerToUserDetails(CustomerDto customer) {
        UserStatus status = UserStatus.valueOf(customer.getStatus());
        System.out.println("status " + status);
        return new org.springframework.security.core.userdetails.User(
                customer.getEmail(), customer.getPassword(),
                status.equals(UserStatus.ACTIVE),
                status.equals(UserStatus.ACTIVE),
                status.equals(UserStatus.ACTIVE),
                status.equals(UserStatus.ACTIVE),
                Role.valueOf(customer.getRole()).getAuthorities()
        );
    }
}