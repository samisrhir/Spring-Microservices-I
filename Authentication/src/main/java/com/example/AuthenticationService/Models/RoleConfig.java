package com.example.AuthenticationService.Models;
import com.example.AuthenticationService.Enums.ERole;
import com.example.AuthenticationService.Repos.RoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class RoleConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (ERole roleEnum : ERole.values()) {
                roleRepository.findByName(roleEnum).orElseGet(() -> {
                    Role role = new Role(roleEnum);
                    return roleRepository.save(role);
                });
            }
        };
    }
}
