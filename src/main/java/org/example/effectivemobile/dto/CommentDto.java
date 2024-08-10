package org.example.effectivemobile.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link org.example.effectivemobile.entity.Comment}
 */
@Value
@Builder
public class CommentDto implements Serializable {
    String text;
    UUID userId;
}