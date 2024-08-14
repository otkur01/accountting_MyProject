package com.cydeo.service.impl;

import com.cydeo.dto.*;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {


    private final MapperUtil mapperUtil;
    private final InvoiceRepository invoiceRepository;
    private final ClientVendorRepository clientVendorRepository;

    private final SecurityService securityService;
    private final ProductService productService;


    private final InvoiceProductService invoiceProductService;

    public InvoiceServiceImpl(MapperUtil mapperUtil, InvoiceRepository invoiceRepository, ClientVendorRepository clientVendorRepository, SecurityService securityService, @Lazy ProductService productService, InvoiceProductService invoiceProductService) {
        this.mapperUtil = mapperUtil;

        this.invoiceRepository = invoiceRepository;
        this.clientVendorRepository = clientVendorRepository;
        this.securityService = securityService;
        this.productService = productService;

        this.invoiceProductService = invoiceProductService;
    }


//    public List<InvoiceDto> listAllByCompanyAndInvoiceType(Company company, InvoiceType invoiceType) {
//        List<Invoice> purchaseInvoices = InvoiceRepository.findAllByCompanyEqualsAndAndInvoiceType(company, invoiceType);
//        return purchaseInvoices.stream().map(invoiceMapper::convertToDTO).collect(Collectors.toList());
//    }


    @Override
    public List<InvoiceDto> listAllPurchaseInvoices(InvoiceType invoiceType) {
        List<Invoice> purchaseInvoiceList = invoiceRepository.findAllByInvoiceTypeOrderByDate(invoiceType);

        return purchaseInvoiceList.stream()
                .filter(invoice -> invoice.getCompany().getId() == securityService.getLoggedInUser().getCompany().getId())
                .sorted(Comparator.comparing(Invoice::getDate).reversed())
                .map(purchase -> mapperUtil.convert(purchase, new InvoiceDto()))
                .map(invoiceDto -> {
                    invoiceDto.setTotal(getTotalForInvoice(invoiceProductService.getAllByInvoiceId(invoiceDto.getId())));
                    invoiceDto.setTax(getTotalTaxForInvoice(invoiceProductService.getAllByInvoiceId(invoiceDto.getId())));
                    invoiceDto.setPrice(getTotalPriceForInvoice(invoiceProductService.getAllByInvoiceId(invoiceDto.getId())));
                    return invoiceDto;
                })

                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> listAllSalesInvoices(InvoiceType invoiceType) {
        List<Invoice> purchaseInvoiceList = invoiceRepository.findAllByInvoiceTypeOrderByDate(invoiceType);

        return purchaseInvoiceList.stream()
               .filter(invoice -> invoice.getIsDeleted().equals(false))
                .filter(invoice -> invoice.getCompany().getId() == securityService.getLoggedInUser().getCompany().getId())
                .sorted(Comparator.comparing(Invoice::getDate).reversed())
                .map(purchase -> mapperUtil.convert(purchase, new InvoiceDto()))
                .map(invoiceDto -> {
//                    invoiceDto.setPrice(totalPriceForInvoice(invoiceDto));
//                    invoiceDto.setTax(totalTaxForInvoice(invoiceDto));
//                    invoiceDto.setTotal(invoiceDto.getPrice().add(invoiceDto.getTax()));
//                    return invoiceDto;
                    invoiceDto.setTotal(getTotalForInvoice(invoiceProductService.getAllByInvoiceId(invoiceDto.getId())));
                    invoiceDto.setTax(getTotalTaxForInvoice(invoiceProductService.getAllByInvoiceId(invoiceDto.getId())));
                    invoiceDto.setPrice(getTotalPriceForInvoice(invoiceProductService.getAllByInvoiceId(invoiceDto.getId())));
                    return invoiceDto;

                })
                .collect(Collectors.toList());
    }

    public BigDecimal totalPriceForInvoice(InvoiceDto invoiceDto) {
        return invoiceProductService.getAllByInvoiceId(invoiceDto.getId()).stream()
                .map(invoiceProductDto -> {
                    BigDecimal totalPrice = BigDecimal.valueOf(invoiceProductDto.getQuantity()).multiply(invoiceProductDto.getPrice());
                    return totalPrice;
                })
              .reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));

    }

    @Override
    public BigDecimal totalTaxForInvoice(InvoiceDto invoiceDto) {
        return invoiceProductService.getAllByInvoiceId(invoiceDto.getId()).stream()
                .map(invoiceProductDto -> {
                    BigDecimal totalTax = BigDecimal.valueOf(invoiceProductDto.getQuantity()).multiply(invoiceProductDto.getPrice())
                            .multiply(BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                    return totalTax;
                })
              .reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));

    }

    @Override
    public BigDecimal totalForInvoice(InvoiceDto invoiceDto) {
        return totalPriceForInvoice(invoiceDto).add(totalTaxForInvoice(invoiceDto));

    }

    @Override
    public InvoiceDto getById(Long id) {

        InvoiceDto invoiceDto = mapperUtil.convert(invoiceRepository.findById(id).get(), new InvoiceDto());
        invoiceDto.setPrice(getTotalPriceForInvoice(invoiceProductService.getAllByInvoiceId(id)));
        invoiceDto.setTax(getTotalTaxForInvoice(invoiceProductService.getAllByInvoiceId(id)));
        invoiceDto.setTotal(getTotalForInvoice(invoiceProductService.getAllByInvoiceId(id)));
        return invoiceDto;
    }

    @Override
    public List<InvoiceDto> getInvoiceByCompanyAndInvoiceStatus(InvoiceStatus invoiceStatus, CompanyDto companyDto) {
        return invoiceRepository.findTop3ByInvoiceStatusAndCompanyOrderByDateDesc(invoiceStatus, mapperUtil.convert(companyDto, new Company())).stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getAllInvoiceDtoByClientId(Long id) {
        return invoiceRepository.findAllByClientVendorId(id).stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getAllByCompanyId(Long id) {
        return invoiceRepository.findAllByCompanyId(id).stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalForInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
        BigDecimal total = invoiceProductDtoList.stream()
                .map(invoiceProductDto -> (invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity())))
                        .add(
                                (invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity())).multiply((BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))))

                )
                .reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalTaxForInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
        BigDecimal totalTax = invoiceProductDtoList.stream()
                .map(invoiceProductDto -> invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity())
                        .multiply(BigDecimal.valueOf(invoiceProductDto.getTax())).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))
                .reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));

        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalPriceForInvoice(List<InvoiceProductDto> invoiceProductDtoList) {
        BigDecimal totalPrice = invoiceProductDtoList.stream().map(invoiceProductDto -> invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity()))).reduce(BigDecimal.ZERO, (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public InvoiceDto approve(InvoiceDto invoiceDto) {

        invoiceDto.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceDto.setDate(LocalDate.now());
        invoiceProductService.getAllByInvoiceId(invoiceDto.getId()).stream()
                .map(invoiceProductDto -> {
                    ProductDto productDto = productService.getById(invoiceProductDto.getProduct().getId());
                    Product product = mapperUtil.convert(productDto, new Product());
                    Integer newQuantity = invoiceProductDto.getQuantity();
                    Integer inStock = product.getQuantityInStock();
                    product.setQuantityInStock(newQuantity + inStock);
                    productService.save(mapperUtil.convert(product, new ProductDto()));
                    return invoiceProductDto;
                }).collect(Collectors.toList());

        Invoice invoice = invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    @Override
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public InvoiceDto approveSales(InvoiceDto invoiceDto) {
        invoiceDto.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceDto.setDate(LocalDate.now());
        invoiceProductService.getAllByInvoiceId(invoiceDto.getId()).stream()
                .map(invoiceProductDto -> {
                    ProductDto productDto = productService.getById(invoiceProductDto.getProduct().getId());
                    Product product = mapperUtil.convert(productDto, new Product());
                    Integer newQuantity = invoiceProductDto.getQuantity();
                    Integer inStock = product.getQuantityInStock();
                    product.setQuantityInStock(inStock - newQuantity);
                    productService.save(mapperUtil.convert(product, new ProductDto()));
                    invoiceProductDto.setProfitLoss(
                            invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity()))
                                    .add(invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity())).multiply(
                                            BigDecimal.valueOf(invoiceProductDto.getTax()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)))
                    );
                   invoiceProductService.save(invoiceProductDto);

                    return invoiceProductDto;
                }).collect(Collectors.toList());

        Invoice invoice = invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
        return mapperUtil.convert(invoice, new InvoiceDto());
    }


    //***


    public String generateInvoiceNo() {
        Invoice lastInvoice = invoiceRepository.findTopByOrderByIdDesc();
        int newInvoiceNo = (lastInvoice != null) ? Integer.parseInt(lastInvoice.getInvoiceNo().split("-")[1]) + 1 : 1;
        return String.format("P-%03d", newInvoiceNo);
    }

    public String generateInvoiceNoForSale() {
        Invoice lastInvoice = invoiceRepository.findTopByOrderByIdDesc();
        int newInvoiceNo = (lastInvoice != null) ? Integer.parseInt(lastInvoice.getInvoiceNo().split("-")[1]) + 1 : 1;
        return String.format("S-%03d", newInvoiceNo);
    }

    @Override
    public List<InvoiceDto> listAllByCompanyAndInvoiceType(CompanyDto companyDto, InvoiceType invoiceType, InvoiceStatus invoiceStatus) {
        Company company = mapperUtil.convert(companyDto, new Company());
        return invoiceRepository.findByCompanyAndInvoiceTypeAndInvoiceStatus(company, invoiceType, invoiceStatus).stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDto())).collect(Collectors.toList());
    }

    public InvoiceDto save(InvoiceDto invoiceDto) {
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        Invoice invoice = mapperUtil.convert(invoiceDto, new Invoice());
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceRepository.save(invoice);
        return mapperUtil.convert(invoice, new InvoiceDto());
    }

    public List<ClientVendorDto> getAllVendors() {
        return clientVendorRepository.findAll()
                .stream().map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
                .collect(Collectors.toList());
    }
}