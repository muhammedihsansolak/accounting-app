package com.cydeo.controller;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    //    user_list page
    //    End-user should be able to List (display) all Users in the user_list page...
    @GetMapping("/list")
    public String listUser(Model model) {
        List<UserDTO> userDTOList = userService.listAllUsers();
        model.addAttribute("users", userDTOList);
        return "/user/user-list";

    }

    //    End-user should be able to Edit each User, when click on Edit button, end-user should land on user_update
    //    page and the edit form should be populated with the information of that very same User.
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        UserDTO user = userService.findUserById(id);
        model.addAttribute("users", user);
        return "/user/user-update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") UserDTO userDtoToUpdate) {
        UserDTO userDTO = userService.findUserById(id);
        userService.update(userDTO, userDtoToUpdate);

        return "redirect:/user/user-list";
    }

    //    End-user should be able to Delete each User (soft delete), then end up to the user_list page with updated User list.
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/user/user-list";
    }

    //    When End-User clicks on "Create-User" button, user_create page should be displayed with an Empty user form,
    @PostMapping("/create")
    public String createUser(@ModelAttribute UserDTO userDTO) {
        userService.save(userDTO);
        return "redirect:/user/user-list";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("newUser", new UserDTO());
        return "user/user-create";
    }


}