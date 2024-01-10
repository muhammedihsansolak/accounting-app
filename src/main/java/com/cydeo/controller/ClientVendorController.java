package com.cydeo.controller;

import com.cydeo.entity.ClientVendor;
import com.cydeo.service.ClientVendorService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client-vendor")
public class ClientVendorController {

    public ClientVendorService clientVendorService;
    @GetMapping("/list")
    public String listClientVendors(Model model) {
        List<ClientVendor> clientVendors = clientVendorService.getAllClientVendors();
        model.addAttribute("clientVendors", clientVendors);
        return "clientVendor_list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("clientVendor", new ClientVendor());
        return "clientVendor_create";
    }
    @PostMapping("/create")
    public String createClientVendor(@ModelAttribute("clientVendor") ClientVendor clientVendor) {
        clientVendorService.saveClientVendor(clientVendor);
        return "redirect: client-vendors/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ClientVendor clientVendor = clientVendorService.getClientVendorById(id);
        model.addAttribute("clientVendor", clientVendor);
        return "clientVendor_update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id, @ModelAttribute("clientVendor") ClientVendor clientVendor) {
        clientVendor.setId(id);
        clientVendorService.saveClientVendor(clientVendor);
        return "clientVendor_update";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id) {
        clientVendorService.deleteClientVendor(id);
        return "clientVendor_list";


    }

}
