package org.example.effectivemobile.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.effectivemobile.entity.enums.TaskPriority;
import org.example.effectivemobile.entity.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank(message = "Can not be blank")
    @NotEmpty(message = "Can not be empty")
    @NotNull(message = " Can not be null")
    private String title;

    @NotBlank(message = "Can not be blank")
    @NotEmpty(message = "Can not be empty")
    @NotNull(message = " Can not be null")
    private String body;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    private User author;

    @ManyToOne
    private User executor;

}