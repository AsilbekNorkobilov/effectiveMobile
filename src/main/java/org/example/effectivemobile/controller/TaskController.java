package org.example.effectivemobile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.effectivemobile.dto.CommentDto;
import org.example.effectivemobile.dto.ExceptionDto;
import org.example.effectivemobile.dto.TaskDto;
import org.example.effectivemobile.entity.enums.TaskStatus;
import org.example.effectivemobile.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/task")
@Tag(name = "Task API",description = "For save,get,delete and edit tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create a new task",description = "Creates a new task with the provided details. Accessible only to users with the 'ROLE_AUTHOR' role")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Task successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @PostMapping()
    public HttpEntity<?> saveTask(@RequestBody TaskDto taskDto){
        return taskService.saveTask(taskDto);
    }

    @Operation(summary = "Retrieve all tasks with pagination and sorting",description = "Fetches a paginated and sorted list of tasks. Supports pagination and sorting parameters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of tasks",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping
    public HttpEntity<?> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "id") String sortBy){
        Pageable pageable=PageRequest.of(page,size,Sort.by(sortBy));
        return taskService.getAllTasks(pageable);
    }


    @Operation(summary = "Retrieve a task by its ID",description = "Fetches a task based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("{id}")
    public HttpEntity<?> getTask(@PathVariable UUID id){
        return taskService.getTask(id);
    }


    @Operation(summary = "Retrieve tasks for a specific user with pagination and sorting",
            description = "Fetches a paginated and sorted list of tasks for a user identified by the provided ID. Supports pagination and sorting parameters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of tasks for the user",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @GetMapping("user/{id}")
    public HttpEntity<?> getUserTasks(@PathVariable UUID id,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy
    ){
        Pageable pageable=PageRequest.of(page,size,Sort.by(sortBy));
        return taskService.getUserTasks(id,pageable);
    }


    @Operation(summary = "Update a task by its ID",description = "Updates the details of an existing task identified by the provided ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PutMapping("{id}")
    public HttpEntity<?> editTask(@PathVariable UUID id, @RequestBody TaskDto taskDto){
        return taskService.editTask(id,taskDto);
    }


    @Operation(summary = "Add a comment to a task",description = "Adds a comment to the task identified by the provided task ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment successfully added to the task",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PostMapping("{taskId}/comment")
    public HttpEntity<?> addCommentToTask(@PathVariable UUID taskId, @RequestBody CommentDto commentDto){
        return taskService.addComment(taskId,commentDto);
    }


    @Operation(summary = "Delete a task by its ID",description = "Deletes the task identified by the provided ID. Requires 'ROLE_AUTHOR' role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @DeleteMapping("{id}")
    public HttpEntity<?> deleteTask(@PathVariable UUID id){
        return taskService.deleteTask(id);
    }


    @Operation(summary = "Update the status of a task",description = "Updates the status of the task identified by the provided ID. Requires 'ROLE_EXECUTOR' role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status value or request body",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExceptionDto.class)))
    })
    @PreAuthorize("hasRole('ROLE_EXECUTOR')")
    @PatchMapping("{id}/status")
    public HttpEntity<?> updateTaskStatus(@PathVariable UUID id, @RequestBody TaskStatus status) {
        return taskService.changeStatus(id,status);
    }

}
