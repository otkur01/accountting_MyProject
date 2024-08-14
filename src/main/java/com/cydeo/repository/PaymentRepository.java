package com.cydeo.repository;

import com.cydeo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

   List<Payment> findAllByCompanyIdAndYearOrderByMonth(Long id, Integer year);
}
