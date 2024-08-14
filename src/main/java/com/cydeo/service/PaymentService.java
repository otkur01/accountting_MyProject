package com.cydeo.service;

import com.cydeo.dto.PaymentDTO;
import com.stripe.model.PaymentIntent;

import java.util.List;

public interface PaymentService {
    List<PaymentDTO> listPayment(Integer selectedYear);

    PaymentDTO findById(Long id);

    PaymentIntent createPaymentIntent(Long amount, Long paymentId);
}
