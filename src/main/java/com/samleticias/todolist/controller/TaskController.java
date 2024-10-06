package com.samleticias.todolist.controller;

import com.samleticias.todolist.entities.Task;
import com.samleticias.todolist.repositories.TaskRepository;
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
    public ResponseEntity createTask(@RequestBody Task task, HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        task.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getFinishAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data inválida.");
        }

        if (task.getStartAt().isAfter(task.getFinishAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data inválida.");
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

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody Task taskModel, HttpServletRequest request, @PathVariable UUID id ){

        var task = this.taskRepository.findById(id).orElse(null);

        if (task==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada.");
        }
        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sem permissão.");
        }

        PropertyCopyUtils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.ok().body(taskUpdated);
    }
}