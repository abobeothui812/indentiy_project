package com.example.ducanh.controller;

import com.example.ducanh.dto.reponse.ApiResponse;
import com.example.ducanh.dto.reponse.PermissionResponse;
import com.example.ducanh.dto.reponse.RoleResponse;
import com.example.ducanh.dto.request.RoleRequest;
import com.example.ducanh.entity.Permissions;
import com.example.ducanh.entity.Roles;
import com.example.ducanh.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/role")
public class RoleController {

    RoleService roleService;
    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        var result = roleService.createRole(request);

        return ApiResponse.<RoleResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles() {
        var result = roleService.getAllRoles();

        return ApiResponse.<List<RoleResponse>>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);

        return ApiResponse.<Void>builder()
                .build();
    }
}
