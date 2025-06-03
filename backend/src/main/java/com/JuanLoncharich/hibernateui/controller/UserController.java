package com.JuanLoncharich.hibernateui.controller;

import com.JuanLoncharich.hibernateui.model.User;
import com.JuanLoncharich.hibernateui.model.Registro;
import com.JuanLoncharich.hibernateui.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}