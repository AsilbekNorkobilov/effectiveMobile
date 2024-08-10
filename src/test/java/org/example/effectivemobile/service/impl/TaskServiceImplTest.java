package org.example.effectivemobile.service.impl;

import org.example.effectivemobile.dto.CommentDto;
import org.example.effectivemobile.dto.TaskDto;
import org.example.effectivemobile.entity.Comment;
import org.example.effectivemobile.entity.Task;
import org.example.effectivemobile.entity.User;
import org.example.effectivemobile.entity.enums.TaskPriority;
import org.example.effectivemobile.entity.enums.TaskStatus;
import org.example.effectivemobile.mapper.CommentMapper;
import org.example.effectivemobile.mapper.TaskMapper;
import org.example.effectivemobile.repo.CommentRepository;
import org.example.effectivemobile.repo.TaskRepository;
import org.example.effectivemobile.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private UserRepository userRepository;
    private CommentMapper commentMapper;
    private CommentRepository commentRepository;
    private TaskServiceImpl taskService;

    @BeforeEach
    public void before(){
        taskRepository=Mockito.mock(TaskRepository.class);
        taskMapper=Mockito.mock(TaskMapper.class);
        userRepository=Mockito.mock(UserRepository.class);
        commentMapper=Mockito.mock(CommentMapper.class);
        commentRepository=Mockito.mock(CommentRepository.class);
        taskService=new TaskServiceImpl(taskRepository,taskMapper,userRepository,commentRepository,commentMapper);
    }

    //@Test
    void saveTask() {
        UUID id=UUID.randomUUID();
        TaskDto taskDto=TaskDto.builder()
                .body("aa")
                .title("aa")
                .build();
        Task task=Task.builder()
                .id(id)
                .body("aa")
                .title("aa")
                .build();
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        HttpEntity<?> httpEntity = taskService.saveTask(taskDto);
        UUID uuid = (UUID) httpEntity.getBody();
        Assertions.assertEquals(uuid,id);
    }

    @Test
    void getAllTasks() {
        UUID id=UUID.randomUUID();
        Task task=Task.builder()
                .id(id)
                .body("aa")
                .title("aa")
                .build();
        TaskDto taskDto = TaskDto.builder()
                .title("aa")
                .body("aa")
                .build();
        Pageable pageable= PageRequest.of(1,2);
        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task), pageable, 1);
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(task)).thenReturn(taskDto);
        HttpEntity<?> entity = taskService.getAllTasks(pageable);
        Page<TaskDto> body = (Page<TaskDto>) entity.getBody();
        List<TaskDto> tasks = body.getContent();
        Assertions.assertEquals(1,tasks.size());
    }

    @Test
    void editTask() {
        UUID taskId = UUID.randomUUID();
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setBody("Old Body");
        existingTask.setTitle("Old Title");
        existingTask.setPriority(TaskPriority.LOW);
        existingTask.setStatus(TaskStatus.PENDING);

        User executor = new User();
        UUID executorId = UUID.randomUUID();
        executor.setId(executorId);

        TaskDto taskDto = TaskDto.builder()
                .body("New Body")
                .title("New Title")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.COMPLETED)
                .executorId(executorId)
                .build();


        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(executorId)).thenReturn(Optional.of(executor));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(existingTask);

        HttpEntity<?> response = taskService.editTask(taskId, taskDto);

        assertEquals(taskDto, response.getBody());
        assertEquals("New Body", existingTask.getBody());
        assertEquals("New Title", existingTask.getTitle());
        assertEquals(TaskPriority.HIGH, existingTask.getPriority());
        assertEquals(TaskStatus.COMPLETED, existingTask.getStatus());
        assertEquals(executor, existingTask.getExecutor());
    }

    @Test
    void TaskIdNullWhenEdit(){
        TaskDto taskDto = TaskDto.builder()
                .body("New Body")
                .title("New Title")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.COMPLETED)
                .build();
        UUID id=UUID.randomUUID();
        Assertions.assertThrows(RuntimeException.class,()->{
            taskService.editTask(id,taskDto);
        });
    }

    @Test
    void getTask() {
        UUID id=UUID.randomUUID();
        Task task=Task.builder()
                .id(id)
                .body("aa")
                .title("aa")
                .build();
        TaskDto taskDto = TaskDto.builder()
                .title("aa")
                .body("aa")
                .build();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);
        HttpEntity<?> entity = taskService.getTask(id);
        TaskDto body = (TaskDto) entity.getBody();
        Assertions.assertEquals(body.getTitle(),taskDto.getTitle());
    }

    @Test
    void TaskIdNullWhenGet(){
        UUID id=UUID.randomUUID();
        Assertions.assertThrows(RuntimeException.class,()->{
            taskService.getTask(id);
        });
    }

    @Test
    void deleteTask() {
        UUID id=UUID.randomUUID();
        Task task=Task.builder()
                .id(id)
                .body("aa")
                .title("aa")
                .build();
        Mockito.doNothing().when(taskRepository).delete(task);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        HttpEntity<?> httpEntity = taskService.deleteTask(id);
        UUID body = (UUID) httpEntity.getBody();
        Assertions.assertEquals(id,body);
    }

    @Test
    void TaskIdNullWhenDelete(){
        UUID id=UUID.randomUUID();
        Assertions.assertThrows(RuntimeException.class,()->{
            taskService.deleteTask(id);
        });
    }


    @Test
    void changeStatus() {
        UUID id = UUID.randomUUID();
        Task task = new Task();
        task.setId(id);
        task.setStatus(TaskStatus.PENDING);
        TaskStatus newStatus = TaskStatus.COMPLETED;
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
        HttpEntity<?> response = taskService.changeStatus(id, newStatus);
        TaskStatus body = (TaskStatus) response.getBody();
        assertEquals(newStatus, body);
    }

    @Test
    void TaskIdNullWhenChangeStatus(){
        UUID id=UUID.randomUUID();
        Assertions.assertThrows(RuntimeException.class,()->{
            taskService.changeStatus(id,TaskStatus.IN_PROGRESS);
        });
    }

    @Test
    void addComment() {
        UUID taskId = UUID.randomUUID();
        Task task = new Task();
        task.setId(taskId);
        task.setComments(new ArrayList<>());

        CommentDto commentDto = CommentDto.builder()
                .text("aa")
                .build();
        Comment comment = Comment.builder()
                .text("aa")
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentMapper.toEntity(commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        HttpEntity<?> response = taskService.addComment(taskId, commentDto);
        CommentDto body = (CommentDto) response.getBody();
        // Assert
        assertEquals(commentDto, body);
    }

    @Test
    void getUserTasks() {
        UUID taskId = UUID.randomUUID();
        Task task=Task.builder()
                .id(taskId)
                .build();
        UUID id=UUID.randomUUID();
        Pageable pageable=PageRequest.of(1,2);
        Page<Task> taskPage=new PageImpl<>(List.of(task),pageable,1);
        Mockito.when(taskRepository.findAllByAuthorIdOrExecutorId(id,id,pageable)).thenReturn(taskPage);
        HttpEntity<?> entity = taskService.getUserTasks(id, pageable);
        Page<Task> body = (Page<Task>) entity.getBody();
        List<Task> tasks = body.getContent();
        Assertions.assertEquals(1,tasks.size());
    }
}