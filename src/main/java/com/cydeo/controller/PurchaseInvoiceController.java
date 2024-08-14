package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;

    private final SecurityService securityService;

    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;



    public PurchaseInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService, SecurityService securityService, ProductService productService, InvoiceProductService invoiceProductService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.securityService = securityService;
        this.productService = productService;
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/list")
    public String purchaseInvoiceList(Model model) {

        model.addAttribute("invoices", invoiceService.listAllPurchaseInvoices(InvoiceType.PURCHASE));

        return "invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String getCreatePurchaseInvoice( Model model) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceNo(invoiceService.generateInvoiceNo());
        invoiceDto.setDate(LocalDate.now());

        model.addAttribute("vendors", clientVendorService.getClientAndVendorByCompanyAndTypeOrderedByName(securityService.getLoggedInUser().getCompany(), ClientVendorType.VENDOR));
        model.addAttribute("newPurchaseInvoice", invoiceDto);

        return "invoice/purchase-invoice-create";
    }
    @PostMapping("/create")
    public String createInvoice(@ModelAttribute("invoice")InvoiceDto invoiceDto, Model model){
        invoiceDto.setInvoiceType(InvoiceType.PURCHASE);

         InvoiceDto invoiceDto1 = invoiceService.save(invoiceDto);
//          model.addAttribute("newInvoiceProduct", invoiceDto1);
//          model.addAttribute("products", productService.getAllProducts());
//        model.addAttribute("invoiceProducts", invoiceProductService.getAllInvoiceByCompanyId(securityService.getLoggedInUser().getCompany().getId()));


        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable("id")Long id, Model model){
        model.addAttribute("invoice", invoiceService.getById(id));
        model.addAttribute("vendors", clientVendorService.getClientAndVendorByCompanyAndTypeOrderedByName(securityService.getLoggedInUser().getCompany(), ClientVendorType.VENDOR));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("products", productService.getAllProducts());
      model.addAttribute("invoiceProducts", invoiceProductService.getAllByInvoiceId(id));
        return "/invoice/purchase-invoice-update";
    }

    @PostMapping("/addInvoiceProduct/{id}")
    public String addInvoiceProduct( @PathVariable("id")Long id, @Valid@ModelAttribute("newInvoiceProduct")InvoiceProductDto invoiceProductDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("invoice", invoiceService.getById(id));
            model.addAttribute("vendors", clientVendorService.getClientAndVendorByCompanyAndTypeOrderedByName(securityService.getLoggedInUser().getCompany(), ClientVendorType.VENDOR));
            model.addAttribute("newInvoiceProduct", invoiceProductDto);
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("invoiceProducts", invoiceProductService.getAllByInvoiceId(id));
            return "/invoice/purchase-invoice-update";
        }
        invoiceProductService.save(invoiceProductDto);

        return "redirect:/purchaseInvoices/update/{id}";
    }

    @GetMapping("/removeInvoiceProduct/{id}/{id2}")
    public String removeInvoiceProduct(@PathVariable("id")Long id, @PathVariable("id2")Long id2){
        invoiceProductService.delete(id2);

        return "redirect:/purchaseInvoices/update/{id}";
    }

    @GetMapping("/print/{id}")
    public String printInvoice(@PathVariable("id")Long id, Model model){
        model.addAttribute("company", invoiceService.getById(id).getCompany());
        model.addAttribute("invoice", invoiceService.getById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.getAllByInvoiceId(id));

   return "invoice/invoice_print";
    }

    @GetMapping("/approve/{id}")
    public String approvePurchase(@PathVariable("id")Long id){
        invoiceService.approve(invoiceService.getById(id));
        return "redirect:/purchaseInvoices/list";
    }





    }