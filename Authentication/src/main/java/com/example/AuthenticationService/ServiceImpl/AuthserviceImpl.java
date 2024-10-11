package com.example.AuthenticationService.ServiceImpl;

import com.example.AuthenticationService.Dtos.Requests.LoginRequest;
import com.example.AuthenticationService.Dtos.Requests.RegisterRequest;
import com.example.AuthenticationService.Dtos.Responses.AuthResponse;
import com.example.AuthenticationService.Dtos.Responses.MessageResponse;
import com.example.AuthenticationService.Enums.ERole;
import com.example.AuthenticationService.Models.Role;
import com.example.AuthenticationService.Models.User;
import com.example.AuthenticationService.Repos.RoleRepository;
import com.example.AuthenticationService.Repos.UserRepository;
import com.example.AuthenticationService.Security.jwt.JwtUtils;
import com.example.AuthenticationService.Security.services.UserDetailsImpl;
import com.example.AuthenticationService.Services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthserviceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;

    @Override
    public MessageResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new MessageResponse("Username already exists");
        }

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        // Add ROLE_USER by default to every new user
        Role roleUser = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(
                () -> new RuntimeException("Role User not found")
        );
        roles.add(roleUser);

        if (strRoles != null) {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Role roleAdmin = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(
                            () -> new RuntimeException("Role Admin not found")
                    );
                    roles.add(roleAdmin);
                }
            });
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .roles(roles)
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);
        return new MessageResponse("User registered successfully");
    }


    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = (Set<String>) userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        roles.forEach(role -> System.out.println("Assigned role: " + role));

        return new AuthResponse(
                jwt,
                userDetails.getId(),  // This should be an Integer
                userDetails.getUsername(),  // This is a String
                (Set<String>) roles  // List of roles
        );
    }

}
