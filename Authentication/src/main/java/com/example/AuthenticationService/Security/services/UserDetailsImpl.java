package com.example.AuthenticationService.Security.services;

import com.example.AuthenticationService.Models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserDetailsImpl implements UserDetails {
    private Integer id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Set <SimpleGrantedAuthority> authorities;

    public static UserDetailsImpl build(User user){
        Set<SimpleGrantedAuthority> auth = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toSet());

        System.out.println("Roles retrieved for user " + user.getUsername() + ": " + auth);

        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(auth)
                .build();
    }


    @Override
    public Set<SimpleGrantedAuthority> getAuthorities() {
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
