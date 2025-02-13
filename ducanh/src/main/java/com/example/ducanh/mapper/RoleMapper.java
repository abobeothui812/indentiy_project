package com.example.ducanh.mapper;


import com.example.ducanh.entity.Roles;
import com.example.ducanh.dto.reponse.RoleResponse;
import com.example.ducanh.dto.request.RoleRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target="permissionsSet", ignore = true)
    Roles toRole(RoleRequest request);
    //@Mapping(target="permissionsSet", ignore = true)
    RoleResponse toRoleResponse(Roles permission);
}
