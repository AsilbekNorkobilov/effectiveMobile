package org.example.effectivemobile.config;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobile.entity.Role;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.entity.enums.RoleName;
import org.example.effectivemobile.repo.RoleRepository;
import org.example.effectivemobile.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args) {
        if (ddl.equals("create")) {
            generate();
        }
    }


    private void generate() {
        Role roleAuthor = new Role(1, RoleName.ROLE_AUTHOR);
        Role roleExecutor = new Role(2, RoleName.ROLE_EXECUTOR);
        roleRepository.saveAll(List.of(roleAuthor, roleExecutor));

        User author = User.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@gmail.com")
                .password(passwordEncoder.encode("root123"))
                .roles(List.of(roleAuthor))
                .build();
        User executor = User.builder()
                .firstName("Petr")
                .lastName("Petrov")
                .email("petr@gmail.com")
                .password(passwordEncoder.encode("root123"))
                .roles(List.of(roleExecutor))
                .build();
        userRepository.saveAll(List.of(author,executor));

    }
}
