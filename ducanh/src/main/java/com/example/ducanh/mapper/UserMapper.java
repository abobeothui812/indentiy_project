package com.example.ducanh.mapper;

import com.example.ducanh.dto.reponse.UserResponse;
import com.example.ducanh.dto.request.UpdateUserRequest;
import com.example.ducanh.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;
import com.example.ducanh.entity.Users;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    @Mapping(target = "roles",ignore = true)
    Users toUser(UserCreationRequest request);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "username",ignore = true)
    @Mapping(target = "roles",ignore = true)
    void updateUser(@MappingTarget Users user, UpdateUserRequest request);


    UserResponse toUserResponse(Users user);
}
