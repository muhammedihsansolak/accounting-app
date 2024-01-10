package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.entity.ClientVendor;
import com.cydeo.service.ClientVendorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("/clientVendors")
public class ClientVendorController {

    public ClientVendorService clientVendorService;
    @GetMapping("/list")
    public String listClientVendors(Model model) {
        List<ClientVendorDTO> clientVendors = clientVendorService.getAllClientVendors();
        model.addAttribute("clientVendors", clientVendors);
        return "clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("clientVendor", new ClientVendorDTO());
        return "clientVendor/clientVendor-create";
    }
    @PostMapping("/create")
    public String createClientVendor(@ModelAttribute("clientVendor") ClientVendorDTO clientVendor) {
        clientVendorService.saveClientVendor(clientVendor);
        return "redirect: client-vendors/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ClientVendorDTO clientVendor = clientVendorService.getClientVendorById(id);
        model.addAttribute("newClientVendor", clientVendor);
        return "clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id, @ModelAttribute("newClientVendor") ClientVendorDTO clientVendor) {
       clientVendorService.update(id,clientVendor);
        return "clientVendor/clientVendor-update";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id) {
        clientVendorService.delete(id);
        return "clientVendor/clientVendor-list";


    }

}
