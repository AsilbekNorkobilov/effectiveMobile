package org.example.effectivemobile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobile.dto.ExceptionDto;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Tag(name = "User API",description = "For get Authors, executors and specific user")
public class UserController {
    private final UserService userService;


    @Operation(summary = "Get authors",description = "Retrieve a paginated list of users has role Author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Authors",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid role supplied",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("author")
    public HttpEntity<?> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        Pageable pageable=PageRequest.of(page,size,Sort.by(sortBy));
        return userService.getAuthors(pageable);
    }

    @Operation(summary = "Get executors",description = "Retrieve a paginated list of users has role Executor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of Executors",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid role supplied",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("executor")
    public HttpEntity<?> getAllExecutors(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sortBy){
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy));
        return userService.getExecutors(pageable);
    }

    @Operation(summary = "Get specific user",description = "Retrieve a paginated list of users based on his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid role supplied",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("{id}")
    public HttpEntity<?> getUserById(@PathVariable UUID id){
        return userService.getUser(id);
    }
}
