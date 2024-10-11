package com.example.AuthenticationService.Dtos.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Integer Id;
    private String username;
    private Set<String> roles;



}
