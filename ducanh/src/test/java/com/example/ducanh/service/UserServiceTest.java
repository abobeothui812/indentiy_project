package com.example.ducanh.service;

import com.example.ducanh.dto.reponse.UserResponse;
import com.example.ducanh.dto.request.UserCreationRequest;
import com.example.ducanh.entity.Users;
import com.example.ducanh.exception.AppException;
import com.example.ducanh.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
@TestPropertySource("/test.properties")
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;


    @Autowired
    private UserRepository userRepository;



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
                .id("9a3411c42031")
                .username("test")
                .firstName("test")
                .lastName("test")
                .birthDate(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);
        // THEN

        Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
        Assertions.assertThat(response.getUsername()).isEqualTo("john");
    }

    @Test
    void createUser_userExisted_fail(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode())
                .isEqualTo(1001);
    }

}
