package com.mobile_app_server.dto;

import com.mobile_app_server.consts.Roles;
import com.mobile_app_server.entity.UserEntity;
import com.mobile_app_server.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomUserDetailsDto implements UserDetails {



    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetailsDto(UserEntity userEntity) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        this.username = userEntity.getUsername();
        this.password = getPassword(userEntity);
        this.authorities = getAuthorities(userEntity);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity userEntity) {
        return List.of(new SimpleGrantedAuthority(Roles.fromValue(userEntity.getRole()).getName()));
    }

    private String getPassword(UserEntity userEntity) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        return SecurityUtils.hashPassword(userEntity.getPasswordHash(), userEntity.getPasswordSalt());
        return userEntity.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
