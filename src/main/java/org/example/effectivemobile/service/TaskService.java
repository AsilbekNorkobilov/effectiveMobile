package org.example.effectivemobile.service;

import org.example.effectivemobile.dto.CommentDto;
import org.example.effectivemobile.dto.TaskDto;
import org.example.effectivemobile.entity.enums.TaskStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;

import java.util.UUID;

public interface TaskService {
    HttpEntity<?> saveTask(TaskDto taskDto);

    HttpEntity<?> getAllTasks(Pageable pageable);

    HttpEntity<?> editTask(UUID id, TaskDto taskDto);

    HttpEntity<?> getTask(UUID id);

    HttpEntity<?> deleteTask(UUID id);

    HttpEntity<?> changeStatus(UUID id, TaskStatus status);

    HttpEntity<?> addComment(UUID id, CommentDto commentDto);

    HttpEntity<?> getUserTasks(UUID id, Pageable pageable);
}
