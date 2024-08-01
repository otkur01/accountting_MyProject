package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {


    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;
    private final ClientVendorRepository clientVendorRepository;

    private final SecurityService securityService;

    public InvoiceServiceImpl(MapperUtil mapperUtil, InvoiceRepository invoiceRepository, ClientVendorRepository clientVendorRepository, SecurityService securityService) {
        this.mapperUtil = mapperUtil;

        this.invoiceRepository = invoiceRepository;
        this.clientVendorRepository = clientVendorRepository;
        this.securityService = securityService;
    }



//    public List<InvoiceDto> listAllByCompanyAndInvoiceType(Company company, InvoiceType invoiceType) {
//        List<Invoice> purchaseInvoices = InvoiceRepository.findAllByCompanyEqualsAndAndInvoiceType(company, invoiceType);
//        return purchaseInvoices.stream().map(invoiceMapper::convertToDTO).collect(Collectors.toList());
//    }



    @Override
    public List<InvoiceDto> listAllPurchaseInvoices(InvoiceType invoiceType) {
        List<Invoice> purchaseInvoiceList = invoiceRepository.findAllByInvoiceTypeOrderByDate(InvoiceType.PURCHASE);

        return purchaseInvoiceList.stream()
                .filter(invoice -> invoice.getCompany().getId()==securityService.getLoggedInUser().getCompany().getId())
                .map(purchase->mapperUtil.convert(purchase,new InvoiceDto()))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDto getById(Long id) {
        return mapperUtil.convert(invoiceRepository.findById(id).get(),new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> getInvoiceByCompanyAndInvoiceStatus(InvoiceStatus invoiceStatus, CompanyDto companyDto) {
      return invoiceRepository.findTop3ByInvoiceStatusAndCompanyOrderByDateDesc(invoiceStatus, mapperUtil.convert(companyDto, new Company())).stream().map(invoice -> mapperUtil.convert(invoice,new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getAllInvoiceDtoByClientId(Long id) {
        return invoiceRepository.findAllByClientVendorId(id).stream().map(invoice -> mapperUtil.convert(invoice,new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getAllByCompanyId(Long id) {
        return invoiceRepository.findAllByCompanyId(id).stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto())).collect(Collectors.toList());
    }


    //***


    public String generateInvoiceNo() {
        Invoice lastInvoice = invoiceRepository.findTopByOrderByIdDesc();
        int newInvoiceNo = (lastInvoice != null) ? Integer.parseInt(lastInvoice.getInvoiceNo().split("-")[1]) + 1 : 1;
        return String.format("P-%03d", newInvoiceNo);
    }

    @Override
    public List<InvoiceDto> listAllByCompanyAndInvoiceType(CompanyDto companyDto, InvoiceType invoiceType, InvoiceStatus invoiceStatus) {
        Company company = mapperUtil.convert(companyDto,new Company());
        return invoiceRepository.findByCompanyAndInvoiceTypeAndInvoiceStatus(company,invoiceType, invoiceStatus).stream().map(invoice -> mapperUtil.convert(invoice,new InvoiceDto())).collect(Collectors.toList());
    }

    public void save(InvoiceDto invoiceDto){
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoiceRepository.save(invoice);
    }

    public List<ClientVendorDto> getAllVendors() {
        return clientVendorRepository.findAll().stream().map(clientVendor -> mapperUtil.convert(clientVendor,new ClientVendorDto())).collect(Collectors.toList());
    }
}