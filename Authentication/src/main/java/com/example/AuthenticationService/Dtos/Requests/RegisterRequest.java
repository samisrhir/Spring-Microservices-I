package com.example.AuthenticationService.Dtos.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String username;
    @Size(min = 2, max = 20)
    @NotBlank
    private String email;
    @Size(min = 2, max = 10)
    @NotBlank
    private String password;
    private Set<String> roles = new HashSet<>();


}