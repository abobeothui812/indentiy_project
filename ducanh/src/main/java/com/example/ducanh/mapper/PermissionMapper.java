package com.example.ducanh.mapper;

import com.example.ducanh.dto.reponse.PermissionResponse;
import com.example.ducanh.dto.reponse.UserResponse;
import com.example.ducanh.dto.request.PermissionRequest;
import com.example.ducanh.dto.request.UpdateUserRequest;
import com.example.ducanh.dto.request.UserCreationRequest;
import com.example.ducanh.entity.Permissions;
import com.example.ducanh.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permissions toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permissions permission);

    
}
