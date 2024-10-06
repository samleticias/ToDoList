package com.samleticias.todolist.controller;

import com.samleticias.todolist.entities.Task;
import com.samleticias.todolist.repositories.TaskRepository;
import com.samleticias.todolist.exceptions.InvalidDateException;
import com.samleticias.todolist.exceptions.PermissionDeniedException;
import com.samleticias.todolist.exceptions.TaskNotFoundException;
import com.samleticias.todolist.util.PropertyCopyUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity createTask(@RequestBody Task task, HttpServletRequest request) throws InvalidDateException {
        var idUser = request.getAttribute("idUser");
        task.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getFinishAt())) {
            throw new InvalidDateException("As datas de início e fim devem ser futuras.");
        }

        if (task.getStartAt().isAfter(task.getFinishAt())) {
            throw new InvalidDateException("A data de início não pode ser posterior à data de fim.");
        }
        var newTask = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }

    @GetMapping("/")
    public List<Task> getTasks(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody Task taskModel, HttpServletRequest request, @PathVariable UUID id ) throws TaskNotFoundException, PermissionDeniedException {

        var task = this.taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            throw new PermissionDeniedException(id);
        }

        PropertyCopyUtils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.ok().body(taskUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}