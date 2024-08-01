package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import org.springframework.stereotype.Service;

import java.util.List;



public interface ClientVendorService{

    List<ClientVendorDto> getAllVendors();

    ClientVendorDto getByName(String name);

    ClientVendorDto getById(Long id);

    List<ClientVendorDto> getClienAndVendorByCompanyOrderedByType(CompanyDto company);

    ClientVendorDto save(ClientVendorDto clientVendorDto);

    boolean checkHasInvoice(Long id);

    void deleteClientVendor(Long id);

    ClientVendorDto update(ClientVendorDto clientVendorDto);
}