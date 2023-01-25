package com.example.firstservice.service;

import com.example.firstservice.domain.User;
import com.example.firstservice.repos.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void createUser() {
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        User user = new User();
        user.setUserName("MikhailJ");
        user.setFirstName("Mikhail");
        user.setSecondName("Sergeevich");
        user.setLastName("Petrov");
        user.setEmailAddress("dummy@example.com");
        user.setPhoneNumber("2223344555");

        User savedUser = new User();
        savedUser.setUserName("MikhailJ");
        savedUser.setFirstName("Mikhail");
        savedUser.setSecondName("Sergeevich");
        savedUser.setLastName("Petrov");
        savedUser.setEmailAddress("dummy@example.com");
        savedUser.setPhoneNumber("2223344555");
        savedUser.setId(1L);
        savedUser.setActive(true);

        Mockito.when(userRepo.findByUserName(user.getUserName())).thenReturn(null);
        Mockito.when(userRepo.save(user)).thenReturn(savedUser);

        UserService userService = new UserService(userRepo);

        String result = userService.createUser(user);
        Assertions.assertEquals("User with userName: MikhailJ has been saved in the database with id 1", result);
    }
}