package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;


    public PurchaseInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String purchaseInvoiceList(Model model) {

        model.addAttribute("invoices", invoiceService.listAllPurchaseInvoices(InvoiceType.PURCHASE));

        return "invoice/purchase-invoice-list";
    }

//    @GetMapping("/create")
//    public String createPurchaseInvoice( Model model) {
//        model.addAttribute("newPurchaseInvoice", new InvoiceDto());
//
//        return "invoice/purchase-invoice-create"; //+ invoiceDto.getId();
//    }

    }