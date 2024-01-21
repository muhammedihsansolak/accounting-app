package com.cydeo.repository;

import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findInvoiceByInvoiceTypeAndCompany_TitleAndIsDeletedOrderByInvoiceNoDesc(InvoiceType invoiceType, String companyTitle, boolean isDeleted);

    //find latest Invoice
    Optional<Invoice> findTopByCompany_TitleAndInvoiceTypeOrderByInvoiceNoDesc(String companyTitle, InvoiceType invoiceType);

    //show the last 3 approved invoices of the company  -->Elif add this :)
    List<Invoice> findTop3ByCompanyAndInvoiceStatusAndIsDeletedOrderByDateDesc(Company company, InvoiceStatus invoiceStatus,boolean isDeleted);
    boolean existsByClientVendorId (Long id);

}
