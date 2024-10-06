package com.samleticias.todolist.exceptions;

import java.util.UUID;

public class TaskNotFoundException extends ApplicationException {
    public TaskNotFoundException(UUID id) {
        super("Tarefa não encontrada com id: " + id);
    }
}
