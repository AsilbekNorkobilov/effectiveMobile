package org.example.effectivemobile.repo;

import org.example.effectivemobile.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Page<Task> findAllByAuthorIdOrExecutorId(UUID authorId, UUID executorId, Pageable pageable);
}