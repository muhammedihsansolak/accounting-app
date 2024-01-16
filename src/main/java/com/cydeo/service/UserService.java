package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO findByUsername(String username);

    boolean findByUsernameCheck(String username);

    List<UserDTO>getAllUsers();
    void save(UserDTO user);
    void delete(Long id);
    UserDTO findById(Long id);
    UserDTO updateUser(UserDTO dto);
}
