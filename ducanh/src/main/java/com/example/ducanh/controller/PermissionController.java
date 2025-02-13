package com.example.ducanh.controller;

import com.example.ducanh.dto.reponse.ApiResponse;
import com.example.ducanh.dto.reponse.PermissionResponse;
import com.example.ducanh.dto.request.PermissionRequest;
import com.example.ducanh.service.PermissionService;
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
@RequestMapping("/permission")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        var result = permissionService.create(request);

        return ApiResponse.<PermissionResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        var result = permissionService.getAll();

        return ApiResponse.<List<PermissionResponse>>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> deletePermission(@PathVariable String name) {
        permissionService.Delete(name);

        return ApiResponse.<Void>builder()
                .build();

    }

}
