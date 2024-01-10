package com.cydeo.service;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    UserDTO findByUsername(String username);

    List<UserDTO> listAllUsers();

}
