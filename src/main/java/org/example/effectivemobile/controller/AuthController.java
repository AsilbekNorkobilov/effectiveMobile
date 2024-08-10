package org.example.effectivemobile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.example.effectivemobile.dto.ExceptionDto;
import org.example.effectivemobile.dto.LoginDto;
import org.example.effectivemobile.dto.RegisterDto;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.response.Response;
import org.example.effectivemobile.security.JwtUtil;
import org.example.effectivemobile.service.impl.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication API",description = "For sign up,login, sending verification code")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Operation(summary = "Register new User",description = "Registers a new user and returns a JWT token for authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered and token generated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",schema =@Schema(implementation = ExceptionDto.class) )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDto.class)))
    })
    @Transactional
    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto registerDto) {
        String email=authService.register(registerDto);
        return ResponseEntity.ok(
                Response.builder().message("Token").data("Bearer " + jwtUtil.generateToken(email)).build()
        );
    }

    @Operation(summary = "Authenticate user and generate JWT token",
            description = "Authenticates a user based on the provided credentials and returns a JWT token for authentication.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful and token generated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @Transactional
    @SneakyThrows
    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto) {
        String email=authService.login(loginDto);
        return ResponseEntity.ok(
                Response.builder().message("Token").data("Bearer " + jwtUtil.generateToken(email)).build()
        );
    }
}
