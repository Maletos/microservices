package com.example.firstservice.service;

import com.example.firstservice.domain.User;
import com.example.firstservice.repos.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserRepo userRepo;
    private UserService userService;
    @BeforeEach
    public void init() {
        userRepo = Mockito.mock(UserRepo.class);
        userService = new UserService(userRepo);
    }

    @Test
    public void createUser() {
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

        String result = userService.createUser(user);
        Assertions.assertEquals("User with userName: MikhailJ has been saved in the database with id 1", result);
    }

    @Test
    public void createUserWithError() {
        User user = new User();
        user.setUserName("MikhailJ");
        user.setFirstName("Mikhail");
        user.setSecondName("Sergeevich");
        user.setLastName("Petrov");
        user.setEmailAddress("dummy@example.com");
        user.setPhoneNumber("2223344555");

        Mockito.when(userRepo.findByUserName(user.getUserName())).thenReturn(null);
        Mockito.when(userRepo.save(user)).thenThrow(IllegalArgumentException.class);

        Executable executable = () -> userService.createUser(user);

        Assertions.assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void getUsers () {
        User user_1 = new User();
        user_1.setUserName("MikhailJ");
        user_1.setFirstName("Mikhail");
        user_1.setSecondName("Sergeevich");
        user_1.setLastName("Petrov");
        user_1.setEmailAddress("dummy@example.com");
        user_1.setPhoneNumber("2223344555");
        user_1.setId(1L);
        user_1.setActive(true);

        User user_2 = new User();
        user_2.setUserName("MikhailY");
        user_2.setFirstName("Mikhail");
        user_2.setSecondName("Ivanovich");
        user_2.setLastName("Ivanov");
        user_2.setEmailAddress("dummy@example.com");
        user_2.setPhoneNumber("2223344555");
        user_2.setId(2L);
        user_2.setActive(true);
        List<User> userList = new ArrayList<>();
        userList.add(user_1);
        userList.add(user_2);
        Mockito.when(userRepo.findAll()).thenReturn(userList);
        List<User> usersFromDb = userService.getUsers();
        Assertions.assertEquals(2,usersFromDb.size());
    }

    @Test
    public void deleteUser () {
        User user_1 = new User();
        user_1.setUserName("MikhailJ");
        user_1.setFirstName("Mikhail");
        user_1.setSecondName("Sergeevich");
        user_1.setLastName("Petrov");
        user_1.setEmailAddress("dummy@example.com");
        user_1.setPhoneNumber("2223344555");
        user_1.setId(1L);
        user_1.setActive(true);

        User deletedUser = new User();
        deletedUser.setUserName("MikhailJ");
        deletedUser.setFirstName("Mikhail");
        deletedUser.setSecondName("Sergeevich");
        deletedUser.setLastName("Petrov");
        deletedUser.setEmailAddress("dummy@example.com");
        deletedUser.setPhoneNumber("2223344555");
        deletedUser.setId(1L);
        deletedUser.setActive(false);

        Mockito.when(userRepo.existsById(user_1.getId())).thenReturn(true);
        String result = userService.deleteUser(user_1.getId());
        Assertions.assertEquals("User with user id: 1 has been deleted from the database", result);

        Mockito.when(userRepo.findById(deletedUser.getId())).thenReturn(Optional.of(deletedUser));
        User userFromDb = userService.getUser(user_1.getId());
        Assertions.assertEquals(false, userFromDb.isActive());
    }

    @Test
    public void getUserByIdPositive () {
        User user_1 = new User();
        user_1.setUserName("MikhailJ");
        user_1.setFirstName("Mikhail");
        user_1.setSecondName("Sergeevich");
        user_1.setLastName("Petrov");
        user_1.setEmailAddress("dummy@example.com");
        user_1.setPhoneNumber("2223344555");
        user_1.setId(1L);
        user_1.setActive(true);

        Mockito.when(userRepo.existsById(user_1.getId())).thenReturn(true);
        String result = userService.deleteUser(user_1.getId());
        Assertions.assertEquals("User with user id: 1 has been deleted from the database", result);

        Mockito.when(userRepo.findById(user_1.getId())).thenReturn(Optional.of(user_1));
        User userFromDb = userService.getUser(user_1.getId());
        Assertions.assertEquals(userFromDb.getUserName(), user_1.getUserName());
    }

    @Test
    public void etUserByIdNegative() {
        User user_1 = new User();
        user_1.setUserName("MikhailJ");
        user_1.setFirstName("Mikhail");
        user_1.setSecondName("Sergeevich");
        user_1.setLastName("Petrov");
        user_1.setEmailAddress("dummy@example.com");
        user_1.setPhoneNumber("2223344555");
        user_1.setId(1L);

        Mockito.when(userRepo.findById(user_1.getId())).thenReturn(Optional.ofNullable(null));

        Executable executable = () -> userService.getUser(user_1.getId());

        Assertions.assertThrows(ResponseStatusException.class, executable);
    }
}