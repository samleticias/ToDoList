package com.samleticias.todolist.infra;

import com.samleticias.todolist.exceptions.InvalidDateException;
import com.samleticias.todolist.exceptions.PermissionDeniedException;
import com.samleticias.todolist.exceptions.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    private ResponseEntity<RestExceptionMessage> TaskNotFoundExceptionHandler (TaskNotFoundException exception) {
        RestExceptionMessage exceptionResponse = new RestExceptionMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    private ResponseEntity<RestExceptionMessage> PermissionDeniedExceptionHandler (PermissionDeniedException exception) {
        RestExceptionMessage exceptionResponse = new RestExceptionMessage(HttpStatus.FORBIDDEN, exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidDateException.class)
    private ResponseEntity<RestExceptionMessage> InvalidDateExceptionHandler (InvalidDateException exception) {
        RestExceptionMessage exceptionResponse = new RestExceptionMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

}

