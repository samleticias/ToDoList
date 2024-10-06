package com.samleticias.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.samleticias.todolist.entities.User;
import com.samleticias.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity createUser(@RequestBody User userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe.");
        }

        var passwordHashred = BCrypt.withDefaults().hashToString(12,userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }

}
