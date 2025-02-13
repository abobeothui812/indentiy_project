package com.example.ducanh.service;

import com.example.ducanh.dto.reponse.PermissionResponse;
import com.example.ducanh.dto.request.PermissionRequest;
import com.example.ducanh.entity.Permissions;
import com.example.ducanh.mapper.PermissionMapper;
import com.example.ducanh.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {

        Permissions permission = permissionMapper.toPermission(request);

        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);

    }


    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();

        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void Delete(String name){
        permissionRepository.deleteById(name);
    }
}
