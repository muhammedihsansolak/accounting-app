package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.CompanyService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final UserService userService;

    @Override
    public CompanyDTO getCompanyDtoByLoggedInUser() {
        String loggedInUser_userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedInUser_userName != null){
           UserDTO loggedInUserDto = userService.findByUsername(loggedInUser_userName);
           return loggedInUserDto.getCompany();
        }
        return null;
    }
}