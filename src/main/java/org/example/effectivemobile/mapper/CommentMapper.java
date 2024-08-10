package org.example.effectivemobile.mapper;

import org.example.effectivemobile.dto.CommentDto;
import org.example.effectivemobile.entity.Comment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    @Mapping(source = "userId", target = "user.id")
    Comment toEntity(CommentDto commentDto);

    @Mapping(source = "user.id", target = "userId")
    CommentDto toDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    Comment partialUpdate(CommentDto commentDto, @MappingTarget Comment comment);
}