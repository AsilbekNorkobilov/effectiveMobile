package org.example.effectivemobile.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.effectivemobile.entity.Role;
import org.example.effectivemobile.entity.enums.RoleName;

import java.io.Serializable;

/**
 * DTO for {@link org.example.effectivemobile.entity.User}
 */
public record RegisterDto(String firstName, String lastName, @NotNull @NotEmpty String email, @NotNull @NotEmpty String password,
                          RoleName roleName) implements Serializable {
  }