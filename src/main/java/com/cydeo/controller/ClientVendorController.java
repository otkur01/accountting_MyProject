package com.cydeo.controller;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;
    private final SecurityService securityService;

    private final CompanyService companyService;

//        clientVendorTypes.add(ClientVendorType.CLIENT);
//        clientVendorTypes.add(ClientVendorType.VENDOR);

    public ClientVendorController(ClientVendorService clientVendorService, SecurityService securityService, List<ClientVendorType> clientVendorTypes, CompanyService companyService) {
        this.clientVendorService = clientVendorService;
        this.securityService = securityService;
        this.companyService = companyService;
    }


    @GetMapping("/list")
    public String getAllClientVendors(Model model){
        model.addAttribute("clientVendors", clientVendorService.getClienAndVendorByCompanyOrderedByType(securityService.getLoggedInUser().getCompany()));
     return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model){
        model.addAttribute("newClientVendor", new ClientVendorDto());
        model.addAttribute("clientVendorTypes",clientVendorService.clientVendorTypeList() );
        model.addAttribute("countries",companyService.nameAllCountry());

        return "/clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String createNewClientVendor(@Valid@ModelAttribute("newClientVendor") ClientVendorDto clientVendorDto, BindingResult bindingResult, Model model){
       if (bindingResult.hasErrors()){
           model.addAttribute("newClientVendor", clientVendorDto);
           model.addAttribute("clientVendorTypes",clientVendorService.clientVendorTypeList() );
           return "/clientVendor/clientVendor-create";
       }

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
        model.addAttribute("clientVendorTypes",clientVendorService.clientVendorTypeList() );
        model.addAttribute("countries",companyService.nameAllCountry());


        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@Valid@ModelAttribute("clientVendor") ClientVendorDto clientVendorDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("clientVendor", clientVendorDto);
            model.addAttribute("clientVendorTypes",clientVendorService.clientVendorTypeList() );

            return "/clientVendor/clientVendor-update";
        }

      clientVendorService.update(clientVendorDto);


        return "redirect:/clientVendors/list";
    }


}
