package com.cydeo.service;

import com.cydeo.dto.UserDTO;

public interface UserService {
    UserDTO findByUsername(String username);

}
