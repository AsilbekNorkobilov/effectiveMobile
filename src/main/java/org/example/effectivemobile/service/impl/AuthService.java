package org.example.effectivemobile.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.example.effectivemobile.dto.LoginDto;
import org.example.effectivemobile.dto.RegisterDto;
import org.example.effectivemobile.entity.Role;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.entity.enums.RoleName;
import org.example.effectivemobile.mapper.UserLoginMapper;
import org.example.effectivemobile.mapper.UserRegisterMapper;
import org.example.effectivemobile.repo.RoleRepository;
import org.example.effectivemobile.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRegisterMapper userRegisterMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserLoginMapper userLoginMapper;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterDto registerDto) {
        User user = userRegisterMapper.toEntity(registerDto);
        Role selectedRole = roleRepository.findByRoleName(registerDto.roleName().name());
        user.setRoles(List.of(selectedRole));
        userRepository.save(user);
        return user.getEmail();
    }


    public String login(LoginDto loginDto) {
        User user = userLoginMapper.toEntity(loginDto);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password", e);
        }
        return user.getEmail();
    }
}
