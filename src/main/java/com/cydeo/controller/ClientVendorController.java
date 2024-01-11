package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;

    public ClientVendorController(ClientVendorService clientVendorService, UserService userService,CompanyService companyService) {
        this.clientVendorService = clientVendorService;

    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<String> countries = new ArrayList<>();
        countries.addAll(Arrays.asList("UK", "USA"));
        model.addAttribute("newClientVendor", new ClientVendorDTO());
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("countries", countries);
        return "clientVendor/clientVendor-create";

    }

    @GetMapping("/list")
    public String listClientVendors(Model model) {
        List<ClientVendorDTO> clientVendors = clientVendorService.getAllClientVendors();
        model.addAttribute("clientVendors", clientVendors);
        return "clientVendor/clientVendor-list";
    }
    @PostMapping("/create")
    public String createClientVendor(@ModelAttribute("newClientVendor") ClientVendorDTO clientVendor) {
        clientVendorService.saveClientVendor(clientVendor);

        return "redirect: client-vendors/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ClientVendorDTO clientVendor = clientVendorService.findById(id);
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("clientVendor", clientVendor);
        return "clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id, @ModelAttribute("ClientVendor")
    ClientVendorDTO clientVendor) {
       clientVendorService.update(id,clientVendor);
        return "clientVendor/clientVendor-update";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id) {
        clientVendorService.delete(id);
        return "clientVendor/clientVendor-list";


    }

}
