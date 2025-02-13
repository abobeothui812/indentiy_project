package com.example.ducanh.controller;

import com.example.ducanh.dto.reponse.UserResponse;
import com.example.ducanh.dto.request.UserCreationRequest;
import com.example.ducanh.entity.Users;
import com.example.ducanh.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@TestPropertySource("/test.properties")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private LocalDate dob;
    private Users user;


    @BeforeEach
    void initData(){

        dob = LocalDate.of(1999,12,12);

            request = UserCreationRequest.builder()
                    .username("test")
                    .password("12345678")
                    .firstName("test")
                    .lastName("test")
                    .birthDate(dob)
                    .build();

            userResponse = UserResponse.builder()
                    .id("9a3411c42031")
                    .username("test")
                    .firstName("test")
                    .lastName("test")
                    .birthDate(dob)
                    .build();

        user = Users.builder()
                .id("cf0600f538b3")
                .username("test")
                .firstName("test")
                .lastName("test")
                .birthDate(dob)
                .build();
    }


    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id")
                        .value("9a3411c42031")

                );
    }

    @Test
    void createUser_UsernameInvalidRequest_success() throws Exception {
        // GIVEN
        request.setUsername("te");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1002))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("password must be at least 4 characters")

                );
    }


}
