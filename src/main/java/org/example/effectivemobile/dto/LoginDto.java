package org.example.effectivemobile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link org.example.effectivemobile.entity.User}
 */
public record LoginDto(@NotNull @NotEmpty String email, @NotNull @NotBlank String password) implements Serializable {
}