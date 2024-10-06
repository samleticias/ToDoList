package com.samleticias.todolist.exceptions;

import java.util.UUID;

public class PermissionDeniedException extends ApplicationException {
    public PermissionDeniedException(UUID taskId) {
        super("Permissão negada para acessar a tarefa com id: " + taskId);
    }
}
