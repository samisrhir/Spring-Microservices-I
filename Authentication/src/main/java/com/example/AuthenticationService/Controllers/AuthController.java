package com.example.AuthenticationService.Controllers;

import com.example.AuthenticationService.Dtos.Requests.LoginRequest;
import com.example.AuthenticationService.Dtos.Requests.RegisterRequest;
import com.example.AuthenticationService.Dtos.Responses.AuthResponse;
import com.example.AuthenticationService.Dtos.Responses.MessageResponse;
import com.example.AuthenticationService.Services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@AllArgsConstructor
@RestController // Use RestController instead of Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
