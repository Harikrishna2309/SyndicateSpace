package com.example.filesharingapp.syndicate.controller;

import com.example.filesharingapp.syndicate.dot.CreateUserRequest;
import com.example.filesharingapp.syndicate.model.Role;
import com.example.filesharingapp.syndicate.model.User;
import com.example.filesharingapp.syndicate.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest createUserRequest) {
        
        Set<Role> roles = new HashSet<>();
        
        for (String roleName : createUserRequest.getRoleNames()) {
            try {
                Role role = Role.valueOf(roleName.toUpperCase());
                roles.add(role);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid role: " + roleName);
            }
        }

        userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword(), roles);
        return ResponseEntity.ok("User created successfully with roles: " + createUserRequest.getRoleNames());
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/edit/{username}")
    public ResponseEntity<String> editUser(@PathVariable String username, @RequestBody CreateUserRequest updateUserRequest) {
        Optional<User> existingUserOptional = userService.getUserByUsername(username);
        if (!existingUserOptional.isPresent()) {
            return ResponseEntity.status(404).body("No user found with username: " + username);
        }

        // Update user details
        User existingUser = existingUserOptional.get();
        Set<Role> roles = new HashSet<>();
        for (String roleName : updateUserRequest.getRoleNames()) {
            try {
                Role role = Role.valueOf(roleName.toUpperCase());
                roles.add(role);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid role: " + roleName);
            }
        }

        userService.updateUser(existingUser, updateUserRequest.getUsername(), updateUserRequest.getPassword(), roles);
        return ResponseEntity.ok("User updated successfully.");
    }
    
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        boolean isDeleted = userService.deleteUserByUsername(username);
        if (isDeleted) {
            return ResponseEntity.ok("User with username: " + username + " deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("No user found with username: " + username);
        }
    }
    
    
}

