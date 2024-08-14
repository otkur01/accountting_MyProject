package com.cydeo.client;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.CurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url="https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies", name = "Currency-Client")
public interface CurrencyClient {

     @ExecutionTime
    @GetMapping("/usd.json")
    CurrencyDto getAllExchangeRate();

}
