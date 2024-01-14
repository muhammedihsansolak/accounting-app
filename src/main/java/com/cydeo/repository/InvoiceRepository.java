package com.cydeo.repository;

import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findInvoiceByInvoiceTypeAndCompany_Title(InvoiceType invoiceType, String companyTitle);

    //find latest Invoice
    Optional<Invoice> findTopByCompany_TitleOrderByDateDesc(String companyTitle);
}