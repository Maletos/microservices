package com.example.firstservice.service;

import com.example.firstservice.domain.Subscription;
import com.example.firstservice.domain.User;
import com.example.firstservice.repos.SubscriptionRepo;
import com.example.firstservice.repos.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class SubscriptionServiceTest {
    private UserRepo userRepo;
    private UserService userService;
    private SubscriptionService subsService;
    private SubscriptionRepo subsRepo;

    @BeforeEach
    void setUp() {
        userRepo = Mockito.mock(UserRepo.class);
        subsRepo = Mockito.mock(SubscriptionRepo.class);
        userService = new UserService(userRepo);
        subsService = new SubscriptionService(subsRepo, userRepo);
    }

    @Test
    void createSubscription() {
        List<User> userList = getUserList();
        User user_1 = userList.get(0);
        User user_2 = userList.get(1);
        Subscription subscription = new Subscription();
        subscription.setFollower(user_1);
        subscription.setFollowed(user_2);

        Subscription savedSubscription = new Subscription();
        savedSubscription.setFollower(user_1);
        savedSubscription.setFollowed(user_2);
        savedSubscription.setId(1L);
        savedSubscription.setActive(true);

        Mockito.when(userRepo.findByUserName(user_1.getUserName())).thenReturn(user_1);
        Mockito.when(userRepo.findByUserName(user_2.getUserName())).thenReturn(user_2);
        Mockito.when(subsRepo.save(subscription)).thenReturn(savedSubscription);

        String result = subsService.createSubscription(subscription);
        Assertions.assertEquals("Subscription for follower user: MikhailJ and followed user: MikhailY has been saved in the database with id 1", result);

    }

    @Test
    void createSubscriptionWithNotFoundFollwedUser() {
        List<User> userList = getUserList();
        User user_1 = userList.get(0);
        User user_2 = userList.get(1);
        Subscription subscription = new Subscription();
        subscription.setFollower(user_1);
        subscription.setFollowed(user_2);

        Subscription savedSubscription = new Subscription();
        savedSubscription.setFollower(user_1);
        savedSubscription.setFollowed(user_2);
        savedSubscription.setId(1L);
        savedSubscription.setActive(true);

        Mockito.when(userRepo.findByUserName(user_1.getUserName())).thenReturn(user_1);
        Mockito.when(userRepo.findByUserName(user_2.getUserName())).thenReturn(null);

        String result = subsService.createSubscription(subscription);
        Assertions.assertEquals("User with userName: MikhailY not found in the database", result);

    }

    @Test
    void deleteSubscription() {
        List<User> userList = getUserList();
        User user_1 = userList.get(0);
        User user_2 = userList.get(1);
        Subscription subscription = new Subscription();
        subscription.setFollower(user_1);
        subscription.setFollowed(user_2);
        subscription.setId(1L);
        subscription.setActive(false);

        Subscription deleteSubscription = new Subscription();
        deleteSubscription.setFollower(user_1);
        deleteSubscription.setFollowed(user_2);
        deleteSubscription.setId(1L);
        deleteSubscription.setActive(false);

        Mockito.when(subsRepo.existsById(deleteSubscription.getId())).thenReturn(true);
        String result = subsService.deleteSubscription(subscription.getId());
        Assertions.assertEquals("Subscription with subs id: 1 has been deleted from the database", result);

        Mockito.when(subsRepo.findById(deleteSubscription.getId())).thenReturn(Optional.of(deleteSubscription));
        Subscription subsFromDb = subsService.getSubscription(user_1.getId());
        Assertions.assertEquals(false, subsFromDb.isActive());
    }

    @Test
    public void getSubscriptioByIdPositive () {
        List<User> userList = getUserList();
        User user_1 = userList.get(0);
        User user_2 = userList.get(1);
        Subscription subscription = new Subscription();
        subscription.setFollower(user_1);
        subscription.setFollowed(user_2);
        subscription.setId(1L);

        Mockito.when(subsRepo.existsById(subscription.getId())).thenReturn(true);
        Mockito.when(subsRepo.findById(subscription.getId())).thenReturn(Optional.of(subscription));

        Subscription result = subsService.getSubscription(subscription.getId());
        Assertions.assertEquals(subscription.getId(), result.getId());
    }

    @Test
    public void getSubscriptionByIdNegative() {
        List<User> userList = getUserList();
        User user_1 = userList.get(0);
        User user_2 = userList.get(1);
        Subscription subscription = new Subscription();
        subscription.setFollower(user_1);
        subscription.setFollowed(user_2);
        subscription.setId(1L);
        subscription.setActive(true);

        Mockito.when(subsRepo.existsById(subscription.getId())).thenReturn(false);
        Executable executable = () -> subsService.getSubscription(subscription.getId());

        Assertions.assertThrows(ResponseStatusException.class, executable);
    }

    private List<User> getUserList() {
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
        return userList;
    }
}