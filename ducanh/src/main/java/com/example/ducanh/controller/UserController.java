package com.example.ducanh.controller;

import com.example.ducanh.dto.reponse.UserResponse;
import com.example.ducanh.dto.reponse.ApiResponse;
import com.example.ducanh.dto.request.UpdateUserRequest;
import com.example.ducanh.dto.request.UserCreationRequest;
import com.example.ducanh.entity.Users;
import com.example.ducanh.service.UserService;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping("")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        log.info("In method createUser,controller");
        response.setResult(userService.createUser(request));

        return response;
    }

    @GetMapping("/myinfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @GetMapping("")
    ApiResponse<List<Users>> getAllUsers() {
        var authenticate = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username : {}",authenticate.getName());
        authenticate.getAuthorities().forEach(a -> log.info("Role : {}",a.getAuthority()));


        return ApiResponse.<List<Users>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId,@RequestBody UpdateUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,request))
                .build();
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }
}
