package org.example.effectivemobile.mapper;

import org.example.effectivemobile.entity.Task;
import org.example.effectivemobile.dto.TaskDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,uses = CommentMapper.class)
public interface TaskMapper {
    @Mapping(source = "executorId", target = "executor.id")
    @Mapping(source = "authorId", target = "author.id")
    Task toEntity(TaskDto taskDto);

    @AfterMapping
    default void linkComments(@MappingTarget Task task) {
        task.getComments().forEach(comment -> comment.setTask(task));
    }

    @InheritInverseConfiguration(name = "toEntity")
    TaskDto toDto(Task task);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task partialUpdate(TaskDto taskDto, @MappingTarget Task task);
}