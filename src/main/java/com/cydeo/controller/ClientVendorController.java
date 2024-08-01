package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;
    private final SecurityService securityService;

    public ClientVendorController(ClientVendorService clientVendorService, SecurityService securityService) {
        this.clientVendorService = clientVendorService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String getAllClientVendors(Model model){
        model.addAttribute("clientVendors", clientVendorService.getClienAndVendorByCompanyOrderedByType(securityService.getLoggedInUser().getCompany()));
     return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model){
        model.addAttribute("newClientVendor", new ClientVendorDto());
        List<ClientVendorType>clientVendorTypes = new ArrayList<>();
        clientVendorTypes.add(ClientVendorType.CLIENT);
        clientVendorTypes.add(ClientVendorType.VENDOR);
        model.addAttribute("clientVendorTypes",clientVendorTypes );
        return "/clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String createNewClientVendor(@ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto){
        clientVendorService.save(clientVendorDto);

        return "redirect:/clientVendors/list";

    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id")Long id){

        clientVendorService.deleteClientVendor(id);

        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable("id")Long id, Model model){
        model.addAttribute("clientVendor", clientVendorService.getById(id));
        List<ClientVendorType>clientVendorTypes = new ArrayList<>();
        clientVendorTypes.add(ClientVendorType.CLIENT);
        clientVendorTypes.add(ClientVendorType.VENDOR);
        model.addAttribute("clientVendorTypes",clientVendorTypes );

        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@ModelAttribute("clientVendor") ClientVendorDto clientVendorDto, Long id, Model model){
      clientVendorService.update(clientVendorDto);


        return "redirect:/clientVendors/list";
    }


}
