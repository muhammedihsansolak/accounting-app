package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import org.springframework.stereotype.Service;

public interface UserService {
    UserDTO findByUsername(String username);

}
