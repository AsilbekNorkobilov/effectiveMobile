package org.example.effectivemobile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.example.effectivemobile.entity.Task;
import org.example.effectivemobile.entity.enums.TaskPriority;
import org.example.effectivemobile.entity.enums.TaskStatus;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link Task}
 */
@Value
@Builder
@AllArgsConstructor
public class TaskDto implements Serializable {
    String title;
    String body;
    List<CommentDto> comments;
    TaskStatus status;
    TaskPriority priority;
    UUID authorId;
    UUID executorId;


}