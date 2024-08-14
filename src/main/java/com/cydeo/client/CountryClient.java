package com.cydeo.client;


import com.cydeo.dto.CountryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(url = "https://www.universal-tutorial.com/api", name = "countryClient")
public interface CountryClient {


    @GetMapping("/countries/")
   List<CountryDto>getAllCountries(@RequestHeader("Authorization") String token);
}
