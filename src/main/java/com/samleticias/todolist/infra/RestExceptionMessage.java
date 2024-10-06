package com.samleticias.todolist.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class RestExceptionMessage {
    private HttpStatus status;
    private String message;
}
