package com.cydeo.service.impl;

import com.cydeo.dto.AddressDto;
import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.entity.Address;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Company;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.AddressService;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;

    private final MapperUtil mapperUtil;
    private final AddressService addressService;

    private final InvoiceService invoiceService;

    private final SecurityService securityService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, AddressService addressService, InvoiceService invoiceService, SecurityService securityService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.addressService = addressService;
        this.invoiceService = invoiceService;
        this.securityService = securityService;
    }

    @Override
    public List<ClientVendorDto> getAllVendors() {
        return clientVendorRepository.findAll().stream().map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto())).collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto getByName(String name) {
        return mapperUtil.convert(clientVendorRepository.findByClientVendorName(name), new ClientVendorDto());
    }

    @Override
    public ClientVendorDto getById(Long id) {
        return mapperUtil.convert(clientVendorRepository.findById(id).get(), new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> getClienAndVendorByCompanyOrderedByType(CompanyDto company) {
        return clientVendorRepository.findAllByCompanyOrderByClientVendorType(mapperUtil.convert(company, new Company()))
                .stream().filter(clientVendor -> clientVendor.getIsDeleted().equals(false))
        . map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
                .map(clientVendorDto -> {
                    clientVendorDto.setHasInvoice(checkHasInvoice(clientVendorDto.getId()));
                    return clientVendorDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto save(ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        AddressDto addressDto = clientVendorDto.getAddress();
        clientVendor.setCompany(mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company()));
        clientVendor.setInsertUserId(securityService.getLoggedInUser().getId());
        clientVendor.setInsertDateTime(LocalDateTime.now());
        clientVendor.setAddress(mapperUtil.convert(addressDto, new Address()));
        addressService.save(addressDto);
        clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public boolean checkHasInvoice(Long id) {
        List<InvoiceDto>invoiceDtoList = invoiceService.getAllInvoiceDtoByClientId(id);
        return !invoiceDtoList.isEmpty();
    }

    @Override
    public void deleteClientVendor(Long id) {
        ClientVendorDto clientVendorDto = getById(id);

        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto,new ClientVendor());
        clientVendor.setIsDeleted(true);
        clientVendor.setId(id);
        clientVendor.setInsertDateTime(clientVendorRepository.findById(id).get().insertDateTime);
        clientVendor.setInsertUserId(clientVendorRepository.findById(id).get().insertUserId);
        clientVendorRepository.save(clientVendor);
        //save(mapperUtil.convert(clientVendor, new ClientVendorDto()));
    }

    @Override
    public ClientVendorDto update(ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        ClientVendor convertedClient = clientVendorRepository.findById(clientVendorDto.getId()).orElseThrow();
        clientVendor.setInsertUserId(convertedClient.getInsertUserId());
        clientVendor.setInsertDateTime(convertedClient.insertDateTime);
        clientVendor.setCompany(convertedClient.getCompany());
        clientVendorRepository.save(clientVendor);

        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }
}
