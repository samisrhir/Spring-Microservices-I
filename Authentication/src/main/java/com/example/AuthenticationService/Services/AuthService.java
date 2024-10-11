package com.example.AuthenticationService.Services;

import com.example.AuthenticationService.Dtos.Requests.LoginRequest;
import com.example.AuthenticationService.Dtos.Requests.RegisterRequest;
import com.example.AuthenticationService.Dtos.Responses.AuthResponse;
import com.example.AuthenticationService.Dtos.Responses.MessageResponse;

public interface AuthService {

    MessageResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);

}
