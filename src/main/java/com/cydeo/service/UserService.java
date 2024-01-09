package com.cydeo.service;

import java.nio.file.AccessDeniedException;

public interface UserService {
    UserDTO findByUserName(String username) throws AccessDeniedException;

}
