package com.example.firstservice.controller;

import com.example.firstservice.domain.User;
import com.example.firstservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("createUser")
    @Operation(summary = "Create new user")
    public String createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/getUserList")
    @Operation(summary = "Get users list")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/getUser/{id}")
    @Operation(summary = "Get users by user surrogate key")
    public  User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/putUser/{id}")
    @Operation(summary = "Update user information")
    public String updateUser (@RequestBody User user, @PathVariable Long id) {
        return userService.updateUser(user,id);
    }

    @DeleteMapping("/deleteUser/{id}")
    @Operation(summary = "Delete user")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
