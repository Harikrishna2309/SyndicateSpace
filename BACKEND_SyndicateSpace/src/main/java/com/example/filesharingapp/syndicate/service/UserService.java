package com.example.filesharingapp.syndicate.service;

import com.example.filesharingapp.syndicate.model.Role;
import com.example.filesharingapp.syndicate.model.User;
import com.example.filesharingapp.syndicate.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    
    public void createUser(String username, String password, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); 
        user.setRoles(roles);

        userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public void updateUser(User user, String newUsername, String newPassword, Set<Role> newRoles) {
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        user.setRoles(newRoles);
        userRepository.save(user);
    }
    
    public boolean deleteUserByUsername(String username) {
        return userRepository.findByUsername(username).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }
}
