package com.cydeo.controller;


import com.cydeo.enums.Currency;
import com.cydeo.service.PaymentService;
import com.cydeo.service.SecurityService;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityService securityService;

    public PaymentController(PaymentService paymentService, SecurityService securityService) {
        this.paymentService = paymentService;
        this.securityService = securityService;
    }


    @GetMapping("/list")
    public String getPaymentList(@RequestParam(value = "year", required = false)Integer year , Model model){
        int selectedYear;
        if(year==null){
            selectedYear=LocalDate.now().getYear();
        }
        else {
            selectedYear=year;
        }


        model.addAttribute("payments", paymentService.listPayment(selectedYear));
        model.addAttribute("year",selectedYear );

        return "/payment/payment-list";
    }

    @GetMapping("/newpayment/{id}")
    public String paymentNew(@PathVariable("id")Long id, Model model){
        model.addAttribute("payment", paymentService.findById(id));
        model.addAttribute("stripePublicKey", "pk_test_51PnRbvI8fChKBPQNsTraEZmUIMn1aiSuJkHVlXyBrPE4bMAQ70JI0wfk4FlmC3uJboXDIbhhEbKFASrk1YQR71Lx00PdDR4nVP");
        model.addAttribute("currency", Currency.USD);
        model.addAttribute("monthId", paymentService.findById(id).getMonth().getId());

        return "/payment/payment-method";
    }

    @PostMapping("/charge/{id}")
    public String paymentSuccess(Model model, @PathVariable("id")Long id){
//         model.addAttribute("payment", paymentDTO);
//         model.addAttribute("monthId", id);
       //  model.addAttribute("chargeId", "ch_3MUUkZBgDORgSOem1ISvdJZQ");

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(25000L, id);

        model.addAttribute("chargeId", paymentIntent.getId());
        model.addAttribute("description", paymentIntent.getDescription());

        return "payment/payment-result";
    }
    @GetMapping("/toInvoice/{id}")
    public String invoice(@PathVariable Long id, Model model) {

        model.addAttribute("payment", paymentService.findById(id));
        model.addAttribute("company", securityService.getLoggedInUser().getCompany());

        return "payment/payment-invoice-print";
    }




}
