package com.example.firstservice.service;

import com.example.firstservice.domain.User;
import com.example.firstservice.repos.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private UserRepo userRepo;
    private static final Logger LOG = LogManager.getLogger(UserService.class);

    public UserService (UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public String createUser(User user) {
        if (userRepo.findByUserName(user.getUserName()) != null) {
            return String.format("User with userName: %s already exists in the database", user.getUserName());
        }
        user.setActive(true);
        User savedUser = userRepo.save(user);
        return String.format("User with userName: %s has been saved in the database with id %s", savedUser.getUserName(), savedUser.getId());
    }

    public String deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userRepo.deleteById(id);
        return String.format("User with user id: %s has been deleted from the database", id);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public User getUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String updateUser(User user, Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User savedUser = userRepo.save(user);
        return String.format("User with userName: %s has been updated", savedUser.getUserName());
    }
}
