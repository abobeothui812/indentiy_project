package com.example.ducanh.service;

import com.example.ducanh.dto.reponse.UserResponse;
import com.example.ducanh.dto.request.UpdateUserRequest;
import com.example.ducanh.dto.request.UserCreationRequest;
import com.example.ducanh.entity.Users;
import com.example.ducanh.exception.AppException;
import com.example.ducanh.exception.ErrorCode;
import com.example.ducanh.repository.RoleRepository;
import com.example.ducanh.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.example.ducanh.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request){
        log.info("In method createUser,service");

        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Users user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Users> getAllUsers(){

        log.info("In method getAllUsers");


        return userRepository.findAll();
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();

         String name = context.getAuthentication().getName();

         Users user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USERNOTFOUND_EXCEPTION));

         return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id){

        log.info("In method getUserById");

        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USERNOTFOUND_EXCEPTION) ));
    }

    public UserResponse updateUser(String userId, UpdateUserRequest request){
        Users user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USERNOTFOUND_EXCEPTION));
        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));


        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

}
