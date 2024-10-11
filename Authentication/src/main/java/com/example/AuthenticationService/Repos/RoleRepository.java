package com.example.AuthenticationService.Repos;

import com.example.AuthenticationService.Enums.ERole;
import com.example.AuthenticationService.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

        Optional<Role> findByName(ERole role);
}
