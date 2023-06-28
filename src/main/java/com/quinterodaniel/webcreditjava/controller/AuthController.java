package com.quinterodaniel.webcreditjava.controller;

import com.quinterodaniel.webcreditjava.dto.UserDto;
import com.quinterodaniel.webcreditjava.entity.User;
import com.quinterodaniel.webcreditjava.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register")
    public ResponseEntity registration(@RequestBody UserDto userDto) throws Exception {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            throw new Exception("");
        }
        userService.saveUser(userDto);
        return ResponseEntity.ok("");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> users() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
