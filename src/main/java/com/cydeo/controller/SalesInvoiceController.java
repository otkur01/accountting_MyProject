package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {
    private final InvoiceService  invoiceService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;

    private final SecurityService securityService;


    public SalesInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService, ProductService productService, InvoiceProductService invoiceProductService, SecurityService securityService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
        this.securityService = securityService;
    }

    @GetMapping("/list")
    public String getAllSalesInvoices(Model model){
        model.addAttribute("invoices", invoiceService.listAllSalesInvoices(InvoiceType.SALES));

       return "invoice/sales-invoice-list";
    }



    @GetMapping("/create")
    public String getCreateSalasForm(Model model){
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(invoiceService.generateInvoiceNoForSale());
        invoiceDto.setDate(LocalDate.now());
        model.addAttribute("newSalesInvoice", invoiceDto);
        model.addAttribute("clients", clientVendorService.getClientAndVendorByCompanyAndTypeOrderedByName(securityService.getLoggedInUser().getCompany(), ClientVendorType.CLIENT));
      return "invoice/sales-invoice-create";
    }


    @PostMapping("/create")
    public String createSalesForm(@ModelAttribute("newSalesInvoice")InvoiceDto invoiceDto){
        invoiceDto.setInvoiceType(InvoiceType.SALES);
        invoiceService.save(invoiceDto);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable("id")Long id, Model model){
        model.addAttribute("invoice", invoiceService.getById(id));
        model.addAttribute("clients", clientVendorService.getClientAndVendorByCompanyAndTypeOrderedByName(securityService.getLoggedInUser().getCompany(), ClientVendorType.CLIENT));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("invoiceProducts", invoiceProductService.getAllByInvoiceId(id));

        return "invoice/sales-invoice-update";
    }
    @PostMapping("/addInvoiceProduct/{id}")
    public String addInvoiceProduct(@PathVariable("id")Long id, @Valid@ModelAttribute("newInvoiceProduct")InvoiceProductDto invoiceProductDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("invoice", invoiceService.getById(id));
            model.addAttribute("vendors", clientVendorService.getClientAndVendorByCompanyAndTypeOrderedByName(securityService.getLoggedInUser().getCompany(), ClientVendorType.VENDOR));
            model.addAttribute("newInvoiceProduct", invoiceProductDto);
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("invoiceProducts", invoiceProductService.getAllByInvoiceId(id));
            return "invoice/sales-invoice-update";
        }

        invoiceProductService.save(invoiceProductDto);

        return "redirect:/salesInvoices/update/{id}";
    }
    @GetMapping("/removeInvoiceProduct/{id}/{id2}")
    public String removeInvoiceProduct(@PathVariable("id")Long id, @PathVariable("id2")Long id2){
        invoiceProductService.delete(id2);

        return "redirect:/salesInvoices/update/{id}";
    }
    @GetMapping("/print/{id}")
    public String printInvoice(@PathVariable("id")Long id, Model model) {
        model.addAttribute("company", invoiceService.getById(id).getCompany());
        model.addAttribute("invoice", invoiceService.getById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.getAllByInvoiceId(id));

        return "invoice/invoice_print";

    }

    @GetMapping("/delete/{id}")
    public String removeInvoiceProduct(@PathVariable("id")Long id){
        invoiceService.delete(id);

        return "redirect:/salesInvoices/list";
    }
    @GetMapping("/approve/{id}")
    public String approvePurchase(@PathVariable("id")Long id){
        invoiceService.approveSales(invoiceService.getById(id));
        return "redirect:/salesInvoices/list";
    }

}
