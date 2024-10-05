package com.samleticias.todolist.controller;

import com.samleticias.todolist.entities.Task;
import com.samleticias.todolist.repositories.TaskRepository;
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

    @GetMapping
    public List<Task> getTasks(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PostMapping
    public ResponseEntity createTask(@RequestBody Task task, HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        task.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getFinishAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data inválida.");
        }

        if (task.getStartAt().isAfter(task.getFinishAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data inválida.");
        }
        var newTask = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }

}
