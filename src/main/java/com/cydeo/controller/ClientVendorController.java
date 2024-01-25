package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.ClientVendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/clientVendors")
@RequiredArgsConstructor
public class ClientVendorController {

    private final ClientVendorService clientVendorService;


    @GetMapping("/list")
    public String listClientVendors(Model model) {
        List<ClientVendorDTO> clientVendors = clientVendorService.getAllClientVendors();
        model.addAttribute("clientVendors", clientVendors);
        return "clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDTO());
        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("countries", clientVendorService.getCountries());
        return "clientVendor/clientVendor-create";

    }

    @PostMapping("/create")
    public String createClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDTO newClientVendor,
                                     BindingResult bindingResult, Model model) {
        bindingResult = clientVendorService.addTypeValidation(newClientVendor.getClientVendorName(),bindingResult);

        if (bindingResult.hasFieldErrors()){
            model.addAttribute("countries", clientVendorService.getCountries());
            model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
            return "clientVendor/clientVendor-create";
        }
        clientVendorService.saveClientVendor(newClientVendor);
        return "redirect:/clientVendors/list";
    }




    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        ClientVendorDTO clientVendor = clientVendorService.findById(id);

        model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
        model.addAttribute("countries",clientVendorService.getCountries());
        model.addAttribute("clientVendor", clientVendor);
        return "clientVendor/clientVendor-update";
    }


    @PostMapping("/update/{id}")
  public String updateClientVendor(@PathVariable("id") Long id,
                                   @Valid @ModelAttribute("clientVendor") ClientVendorDTO clientVendor,
                                   BindingResult bindingResult, Model model) {
      if (bindingResult.hasErrors()) {
          model.addAttribute("clientVendorTypes", Arrays.asList(ClientVendorType.values()));
          model.addAttribute("countries",clientVendorService.getCountries());
          return "clientVendor/clientVendor-update";
      }

      clientVendorService.update(id, clientVendor);
      return "redirect:/clientVendors/list";
  }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id) {
        boolean hasInvoice = clientVendorService.isClientHasInvoice(id);
        clientVendorService.delete(id);
        return "redirect:/clientVendors/list";
    }
}