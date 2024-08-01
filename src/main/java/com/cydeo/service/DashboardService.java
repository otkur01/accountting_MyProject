package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.CurrencyDto;
import com.cydeo.dto.ExchangeRates;
import com.cydeo.dto.InvoiceDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DashboardService {

    BigDecimal getTotalCost();

    BigDecimal getTotalSales();

    BigDecimal getProfitLoss();

    List<InvoiceDto> last3ApprovedInvoiceByCompany();

    ExchangeRates exchangeForUsd();

}
