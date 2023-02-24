package com.example.firstservice.controller;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
class UserControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private WebApplicationContext webContext;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
        jdbcTemplate.execute("ALTER SEQUENCE users_seq RESTART WITH 1");
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        JSONObject request = new JSONObject();
        request.put("userName","VasyaM");
        request.put("emailAddress", "dummy@elogex.com");
        request.put("firstName", "Vasya");
        request.put("secondName", "Petrovich");
        request.put("lastName", "Myslov");
        request.put("password", "test");
        request.put("gender", "man");
        request.put("phoneNumber", "8800222334");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/user/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toString()));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                     .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.equalTo("User with userName: VasyaM has been saved in the database with id 1")));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        createUser();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
               .get("/user/getUser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    @Transactional
    void getUserNegative() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/user/getUser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    void getUserList() throws Exception {
        createUser();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/user/getUserList")
                .accept(MediaType.APPLICATION_JSON));
        MvcResult result = resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        String content = result.getResponse().getContentAsString();
        JSONArray usersArray = new JSONArray(content);
        Assertions.assertEquals(1, usersArray.length());
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        createUser();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/user/deleteUser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.equalTo("User with user id: 1 has been deleted from the database")));
    }

    @Test
    @Transactional
    void deleteUserNegative() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/user/deleteUser/{id}", 1)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    void updateUser() throws Exception {
        createUser();
        JSONObject request = new JSONObject();
        request.put("userName","VasyaM");
        request.put("emailAddress", "dummy@elogex.com");
        request.put("firstName", "Vasya");
        request.put("secondName", "Petrovich");
        request.put("lastName", "Myslov");
        request.put("password", "test");
        request.put("gender", "man");
        request.put("phoneNumber", "3334445556");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
            .put("/user/putUser/{id}", 1)
            .content(request.toString())
            .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.equalTo("User with userName: VasyaM has been updated")));
    }
}