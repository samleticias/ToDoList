package com.samleticias.todolist.repositories;

import com.samleticias.todolist.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByIdUser(UUID idUser);
}
