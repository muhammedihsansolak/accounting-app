package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO findByUsername(String username);
    List<UserDTO>getAllUsers();
    void save(UserDTO user);
    void update(UserDTO userDTO, UserDTO userDtoToUpdate);
    void delete(Long id);
    UserDTO findById(Long id);
}
