package com.cydeo.repository;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {


    ClientVendor findByClientVendorName(String name);

    List<ClientVendor> findAllByCompanyOrderByClientVendorType(Company company);
}
