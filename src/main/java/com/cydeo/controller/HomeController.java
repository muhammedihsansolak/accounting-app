package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SecurityService securityService;

    /**
     * This end-point is for error.html "Go To Home" button. Redirects the users based on their role.
     * @param response
     * @throws IOException
     */
    @GetMapping("/redirectToHome")
    public void redirectToHome(HttpServletResponse response) throws IOException {

        UserDTO loggedInUser = securityService.getLoggedInUser();

        if (loggedInUser.getRole().getDescription().equals("Root User")){
            response.sendRedirect("/companies/list");
        } else if (loggedInUser.getRole().getDescription().equals("Admin")){
            response.sendRedirect("/users/list");
        }else if (loggedInUser.getRole().getDescription().equals("Manager") || loggedInUser.getRole().getDescription().equals("Employee") ){
            response.sendRedirect("/dashboard");
        }

    }
}
