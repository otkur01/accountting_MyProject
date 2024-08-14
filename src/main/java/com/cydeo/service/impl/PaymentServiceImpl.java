package com.cydeo.service.impl;


import com.cydeo.dto.PaymentDTO;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Currency;
import com.cydeo.enums.Months;
import com.cydeo.repository.PaymentRepository;
import com.cydeo.service.PaymentService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final MapperUtil mapperUtil;

    private final SecurityService securityService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.paymentRepository = paymentRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public List<PaymentDTO> listPayment(Integer selectedYear) {
      List<PaymentDTO>paymentDtoList = paymentRepository.findAll()
                .stream().filter(payment -> payment.getYear()==selectedYear)
                .map(payment ->mapperUtil.convert(payment, new PaymentDTO()))
                .collect(Collectors.toList());


        List<Integer> existingMonths = paymentDtoList.stream().map(paymentDto -> paymentDto.getMonth().getId()).toList();

        for (int i = 0; i < 12; i++) {
            if (existingMonths.contains(i+1)) continue;
            PaymentDTO paymentDto = new PaymentDTO();
            paymentDto.setYear(selectedYear);
            paymentDto.setMonth(Months.values()[i]);
            paymentDto.setPaymentDate(LocalDate.of(selectedYear,i+1,15));
            paymentDto.setPaid(false);
            paymentDto.setCompany(securityService.getLoggedInUser().getCompany());
            paymentDto.setAmount(BigDecimal.valueOf(250));

            paymentRepository.save(mapperUtil.convert(paymentDto, new Payment()));
        }

        return paymentRepository.findAllByCompanyIdAndYearOrderByMonth(securityService.getLoggedInUser().getCompany().getId(), selectedYear).stream().map(payment -> mapperUtil.convert(payment, new PaymentDTO()))
                .toList();

    }

    @Override
    public PaymentDTO findById(Long id) {
        return mapperUtil.convert(paymentRepository.findById(id).get(), new PaymentDTO());
    }

    @Override
    public PaymentIntent createPaymentIntent(Long amount, Long paymentId){

        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()-> new NoSuchElementException("Subscription with this month does not exist."));
        Stripe.apiKey = "sk_test_51KnhIOBgDORgSOemupAaZ34jhzUzHk1h27LfoiJU4CispEYiKiqLkQUXKpQt6F6Z74BLy6rmpGO2j7okscRTjNPf00Vg4wyvgQ";

        String description = "Cydeo accounting subscription fee for : " + payment.getYear() + " " + payment.getMonth().getValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount).setDescription(description)
                .setCurrency(Currency.USD.getValue())
                .build();

        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new IllegalArgumentException("Transaction failed. Please try again!");
        }

        payment.setPaid(true);
        payment.setCompanyStripId(paymentIntent.getId());
        paymentRepository.save(payment);

        return paymentIntent;
    }


}
