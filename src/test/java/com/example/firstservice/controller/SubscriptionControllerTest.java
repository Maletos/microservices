package com.example.firstservice.controller;

import com.example.firstservice.domain.Subscription;
import com.example.firstservice.domain.User;
import com.example.firstservice.repos.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase()
@ExtendWith(SpringExtension.class)
public class SubscriptionControllerTest {
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MockMvc mockMvc;
    private User followedUser;
    private User followerUser;
    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();

        followerUser = new User();
        followerUser.setUserName("MikhailJ");
        followerUser.setFirstName("Mikhail");
        followerUser.setSecondName("Sergeevich");
        followerUser.setLastName("Petrov");
        followerUser.setEmailAddress("dummy@example.com");
        followerUser.setPhoneNumber("2223344555");
        followerUser.setPassword("password1");
        followerUser.setId(1L);

        followedUser = new User();
        followedUser.setUserName("MikhailY");
        followedUser.setFirstName("Mikhail");
        followedUser.setSecondName("Ivanovich");
        followedUser.setPassword("password");
        followedUser.setLastName("Ivanov");
        followedUser.setEmailAddress("dummy_yammy@example.com");
        followedUser.setPhoneNumber("2223344555");
        followedUser.setId(2L);

        jdbcTemplate.execute("ALTER SEQUENCE users_seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER SEQUENCE subs_seq RESTART WITH 1");
    }

    @Test
    @Transactional
    void subscribeTest() throws Exception{
        createUsers();
        Subscription subs = new Subscription();
        subs.setFollowed(followedUser);
        subs.setFollower(followerUser);
        ObjectMapper objectMapper = new ObjectMapper();
        String subsJson = objectMapper.writeValueAsString(subs);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/subscription/subscribe/v2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subsJson));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.equalTo("Subscription for follower user: MikhailJ and followed user: MikhailY has been saved in the database with id 1")));

    }


    @Test
    @Transactional
    void getSubscriptionByIDTest() throws Exception{
        subscribeTest();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/subscription/getSubscription/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

    }

    @Test
    @Transactional
    void deleteSubscriptionByIDTest() throws Exception{
        subscribeTest();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/subscription/deleteSubscription/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.equalTo("Subscription with subs id: 1 has been deleted from the database")));

    }


    private void createUsers() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(followerUser);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/user/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.equalTo("User with userName: MikhailJ has been saved in the database with id 1")));

        userJson = objectMapper.writeValueAsString(followedUser);

        resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/user/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.equalTo("User with userName: MikhailY has been saved in the database with id 2")));
    }
}
