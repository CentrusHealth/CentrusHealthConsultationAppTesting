package com.consultationapp.demo.controller;
import com.consultationapp.demo.dto.CreateUserRequest;
import com.consultationapp.demo.dto.UserResponse;
import com.consultationapp.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse
    createUser(
            @RequestBody CreateUserRequest request
    ) {
        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(
            @PathVariable UUID id
    ) {
        return userService.getUserById(id);
    }
}