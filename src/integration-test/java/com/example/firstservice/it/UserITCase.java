package com.example.firstservice.it;

import com.example.firstservice.domain.User;
import com.example.firstservice.repos.UserRepo;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class UserITCase {
    @Autowired
    UserRepo userRepo;

    @Test
    @Transactional
    public void testCreateUser_andCount() {
        User followerUser = new User();
        followerUser.setUserName("MikhailJ");
        followerUser.setFirstName("Mikhail");
        followerUser.setSecondName("Sergeevich");
        followerUser.setLastName("Petrov");
        followerUser.setEmailAddress("dummy@example.com");
        followerUser.setPhoneNumber("2223344555");
        followerUser.setPassword("password1");
        followerUser.setId(1L);

        userRepo.save(followerUser);
        Assertions.assertEquals(1, userRepo.count());

        User followedUser = new User();
        followedUser.setUserName("MikhailY");
        followedUser.setFirstName("Mikhail");
        followedUser.setSecondName("Ivanovich");
        followedUser.setPassword("password");
        followedUser.setLastName("Ivanov");
        followedUser.setEmailAddress("dummy_yammy@example.com");
        followedUser.setPhoneNumber("2223344555");
        followedUser.setId(2L);

        userRepo.save(followedUser);
        Assertions.assertEquals(2, userRepo.count());
    }
}
