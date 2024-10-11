package com.example.AuthenticationService.Models;

import com.example.AuthenticationService.Enums.ERole;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "roles")
@Setter
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role(ERole roleEnum) {
        this.name = roleEnum;
    }
}

