package com.cydeo.service;

import com.cydeo.dto.RoleDTO;

import java.util.List;

public interface RoleService{
    RoleDTO findById(Long id);
    List<RoleDTO> getAllRoles();

    List<RoleDTO>getAllRolesForCurrentUser();

}
