package org.example.effectivemobile.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobile.dto.CommentDto;
import org.example.effectivemobile.dto.TaskDto;
import org.example.effectivemobile.entity.Comment;
import org.example.effectivemobile.entity.Task;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.entity.enums.TaskStatus;
import org.example.effectivemobile.mapper.CommentMapper;
import org.example.effectivemobile.mapper.TaskMapper;
import org.example.effectivemobile.repo.CommentRepository;
import org.example.effectivemobile.repo.TaskRepository;
import org.example.effectivemobile.repo.UserRepository;
import org.example.effectivemobile.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    @Override
    public HttpEntity<?> saveTask(TaskDto taskDto) {
        List<CommentDto> commentsDto = taskDto.getComments();
        Task entity = taskMapper.toEntity(taskDto);
        entity.setComments(new ArrayList<>());
        Task save = taskRepository.save(entity);
        List<Comment> comments=new ArrayList<>();
        for (CommentDto commentDto : commentsDto) {
            Comment comment = commentMapper.toEntity(commentDto);
            comment.setTask(save);
            comments.add(comment);
        }
        commentRepository.saveAll(comments);
        return ResponseEntity.status(HttpStatus.CREATED).body(save.getId());
    }

    @Override
    public HttpEntity<?> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<TaskDto> taskDtoList=new ArrayList<>();
        for (Task task : tasks) {
            taskDtoList.add(taskMapper.toDto(task));
        }
        Page<TaskDto> pagedDto=new PageImpl<>(taskDtoList,pageable,taskDtoList.size());
        return ResponseEntity.ok(pagedDto);
    }

    @Override
    public HttpEntity<?> editTask(UUID id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (taskDto.getExecutorId()!=null) {
            Optional<User> opt = userRepository.findById(taskDto.getExecutorId());
            if (opt.isPresent()){
                User executor = opt.get();
                task.setExecutor(executor);
            }
        }
        if (!taskDto.getBody().isEmpty()){
            task.setBody(taskDto.getBody());
        }
        if (!taskDto.getTitle().isEmpty()){
            task.setTitle(taskDto.getTitle());
        }
        if(taskDto.getPriority()!=null){
            task.setPriority(taskDto.getPriority());
        }
        if (taskDto.getStatus()!=null){
            task.setStatus(taskDto.getStatus());
        }
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(taskDto);
    }

    @Override
    public HttpEntity<?> getTask(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        TaskDto dto = taskMapper.toDto(task);
        return ResponseEntity.ok(dto);
    }

    @Override
    public HttpEntity<?> deleteTask(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @Override
    public HttpEntity<?> changeStatus(UUID id, TaskStatus status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @Override
    public HttpEntity<?> addComment(UUID id, CommentDto commentDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setTask(task);
        commentRepository.save(comment);
        return ResponseEntity.ok(commentDto);
    }

    @Override
    public HttpEntity<?> getUserTasks(UUID id, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllByAuthorIdOrExecutorId(id, id, pageable);
        return ResponseEntity.ok(tasks);
    }
}
