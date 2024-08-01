package com.cydeo.repository;

import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


  Invoice  findTopByOrderByIdDesc();

    List<Invoice> findAllByInvoiceTypeOrderByDate(InvoiceType invoiceType);
     List<Invoice>findByCompanyAndInvoiceTypeAndInvoiceStatus(Company company, InvoiceType invoiceType, InvoiceStatus invoiceStatus);

     List<Invoice> findTop3ByInvoiceStatusAndCompanyOrderByDateDesc(InvoiceStatus invoiceStatus,Company company);

     List<Invoice> findAllByClientVendorId(Long id);


     List<Invoice>findAllByCompanyId(Long id);



}


