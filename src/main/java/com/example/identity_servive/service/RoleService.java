package com.example.identity_servive.service;

import com.example.identity_servive.dto.request.PermissionRequest;
import com.example.identity_servive.dto.request.RoleRequest;
import com.example.identity_servive.dto.response.PermissionResponse;
import com.example.identity_servive.dto.response.RoleRespone;
import com.example.identity_servive.entity.Permission;
import com.example.identity_servive.mapper.PermissionMapper;
import com.example.identity_servive.mapper.RoleMapper;
import com.example.identity_servive.repository.PermissionRepository;
import com.example.identity_servive.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service // Đánh dấu đây là một Bean tầng Service trong Spring
@RequiredArgsConstructor // Tự động tạo Constructor cho các field 'final' (Dependency Injection)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleRespone create(RoleRequest roleRequest) {
        var role = roleMapper.toRole(roleRequest);

        var permissions = permissionRepository.findAllById(roleRequest.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }
    public List<RoleRespone> getAll(){
        return roleRepository.findAll()
                .stream().map(roleMapper::toRoleResponse)
                .toList();
    }
    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
