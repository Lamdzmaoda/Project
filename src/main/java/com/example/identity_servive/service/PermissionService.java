package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.PermissionRequest;
import com.example.identity_servive.dto.response.PermissionResponse;
import com.example.identity_servive.entity.Permission;
import com.example.identity_servive.mapper.PermissionMapper;
import com.example.identity_servive.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service // Đánh dấu đây là một Bean tầng Service trong Spring
@RequiredArgsConstructor // Tự động tạo Constructor cho các field 'final' (Dependency Injection)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;


    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
