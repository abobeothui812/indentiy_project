package com.example.ducanh.service;

import com.example.ducanh.dto.reponse.RoleResponse;
import com.example.ducanh.dto.request.RoleRequest;
import com.example.ducanh.entity.Roles;
import com.example.ducanh.mapper.RoleMapper;
import com.example.ducanh.repository.PermissionRepository;
import com.example.ducanh.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request){
        Roles role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissionsSet());


        role.setPermissionsSet(new HashSet<>(permissions));

        roleRepository.save(role);


        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles(){
        var roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String name){
        roleRepository.deleteById(name);
    }


}
