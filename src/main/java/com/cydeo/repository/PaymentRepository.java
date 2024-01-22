package com.cydeo.repository;

import com.cydeo.entity.Company;
import com.cydeo.entity.Payment;
import com.cydeo.enums.Months;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByCompanyAndMonthAndYear(Company company, Months month, int currentYear);

    List<Payment> findAllByCompany(Company company);
}
