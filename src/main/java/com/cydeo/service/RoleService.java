package com.cydeo.service;

import com.cydeo.dto.RoleDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RoleService {

    RoleDto getRoleByDescription(String description);
    List<RoleDto> getAllRoles();

    RoleDto getRoleById(Long Id);

}
