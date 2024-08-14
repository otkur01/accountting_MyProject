package com.cydeo.service.impl;

import com.cydeo.client.CurrencyClient;
import com.cydeo.dto.*;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final SecurityService securityService;

    private final InvoiceProductService invoiceProductService;
    private final InvoiceService invoiceService;
    private final UserService userService;

    private final CurrencyClient currencyClient;

    public DashboardServiceImpl(SecurityService securityService, InvoiceProductService invoiceProductService, InvoiceService invoiceService, UserService userService, CurrencyClient currencyClient) {
        this.securityService = securityService;
        this.invoiceProductService = invoiceProductService;
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.currencyClient = currencyClient;
    }

    @Override
    public BigDecimal getTotalCost() {
        BigDecimal totalCost;
        String username = securityService.getLoggedInUser().getUsername();
        CompanyDto companyDto = userService.findByUserName(username).getCompany();
        List<InvoiceDto> invoiceDtoList = invoiceService.listAllByCompanyAndInvoiceType(companyDto, InvoiceType.PURCHASE, InvoiceStatus.APPROVED);
        totalCost = invoiceDtoList.stream().map(invoiceDto -> {
            List<InvoiceProductDto> invoiceProductDtoList = invoiceProductService.getAllByInvoiceId(invoiceDto.getId());
           BigDecimal total = invoiceService.totalForInvoice(invoiceDto);
            return total;

        }).reduce(
                BigDecimal::add
        ).orElseThrow();
        return totalCost;
    }

    @Override
    public BigDecimal getTotalSales() {
         BigDecimal totalSales;
        String username = securityService.getLoggedInUser().getUsername();
        CompanyDto companyDto = userService.findByUserName(username).getCompany();
        List<InvoiceDto> invoiceDtoList = invoiceService.listAllByCompanyAndInvoiceType(companyDto, InvoiceType.SALES, InvoiceStatus.APPROVED);
        totalSales = invoiceDtoList.stream().map(invoiceDto -> {
            BigDecimal total = invoiceService.totalForInvoice(invoiceDto);
            return total;
        }).reduce(
                BigDecimal::add
        ).orElseThrow();
        return totalSales;
    }

    @Override
    public BigDecimal getProfitLoss() {
         BigDecimal profitLoss;

        String username = securityService.getLoggedInUser().getUsername();
        CompanyDto companyDto = userService.findByUserName(username).getCompany();
        List<InvoiceDto> invoiceDtoList = invoiceService.listAllByCompanyAndInvoiceType(companyDto,InvoiceType.SALES, InvoiceStatus.APPROVED);
        profitLoss =  invoiceDtoList.stream().map(invoiceDto ->{
           BigDecimal profit = invoiceProductService.getAllByInvoiceId(invoiceDto.getId())
                    .stream().map(InvoiceProductDto::getProfitLoss).reduce(BigDecimal::add).orElseThrow();
           return profit;
        }).reduce(BigDecimal::add).get();

       return profitLoss;
    }



    @Override
    public List<InvoiceDto> last3ApprovedInvoiceByCompany() {
        String username = securityService.getLoggedInUser().getUsername();
        CompanyDto companyDto = userService.findByUserName(username).getCompany();
        List<InvoiceDto>invoiceDtoList = invoiceService.getInvoiceByCompanyAndInvoiceStatus(InvoiceStatus.APPROVED, companyDto);
        invoiceDtoList.stream().map(invoiceDto -> {
            List<InvoiceProductDto> invoiceProductDtoList = invoiceProductService.getAllByInvoiceId(invoiceDto.getId());
            invoiceDto.setPrice(getTotalPriceForInvoice(invoiceProductDtoList));
            invoiceDto.setTax(getTotalTaxForInvoice(invoiceProductDtoList));
            invoiceDto.setTotal(getTotalForInvoice(invoiceProductDtoList));
            return invoiceDto;
        }).collect(Collectors.toList());


        return invoiceDtoList;
    }



    public BigDecimal getTotalForInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
      BigDecimal total =   invoiceProductDtoList.stream()
              .map(invoiceProductDto -> (invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity())))
                      .add(
                              (invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity())).multiply((BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))))

              )
              .reduce(BigDecimal::add).orElseThrow();
       return total.setScale(2,RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalTaxForInvoice(List<InvoiceProductDto> invoiceProductDtoList) {

        BigDecimal totalTax = invoiceProductDtoList.stream()
                .map(invoiceProductDto -> invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity())
                .multiply(BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))
                .reduce(BigDecimal::add).orElseThrow();

        return totalTax.setScale(2,RoundingMode.HALF_UP);

    }

    public BigDecimal getTotalPriceForInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
        BigDecimal totalPrice =   invoiceProductDtoList.stream().map(invoiceProductDto -> invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity()))).reduce(BigDecimal::add).orElseThrow();
        return totalPrice.setScale(2,RoundingMode.HALF_UP);
    }

    public BigDecimal getProfitLossForInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
        BigDecimal totalProfitLoos = invoiceProductDtoList.stream().map(invoiceProductDto -> invoiceProductDto.getProfitLoss()).reduce(BigDecimal::add).orElseThrow();
      return totalProfitLoos.setScale(2, RoundingMode.HALF_UP);
    }


    public ExchangeRates exchangeForUsd(){
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setEuro(currencyClient.getAllExchangeRate().getUsd().getEur());
        exchangeRates.setBritishPound(currencyClient.getAllExchangeRate().getUsd().getGbp());
        exchangeRates.setCanadianDollar(currencyClient.getAllExchangeRate().getUsd().getCad());
        exchangeRates.setIndianRupee(currencyClient.getAllExchangeRate().getUsd().getInr());
        exchangeRates.setJapaneseYen(currencyClient.getAllExchangeRate().getUsd().getJpy());
     return exchangeRates;
    }
    public ExchangeRates exchangeForUsd1(){
        CurrencyDto currencyDto = currencyClient.getAllExchangeRate();
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setEuro(currencyDto.getUsd().getEur());
        exchangeRates.setBritishPound(currencyDto.getUsd().getGbp());
        exchangeRates.setCanadianDollar(currencyDto.getUsd().getCad());
        exchangeRates.setIndianRupee(currencyDto.getUsd().getInr());
        exchangeRates.setJapaneseYen(currencyDto.getUsd().getJpy());
        return exchangeRates;
    }

}
