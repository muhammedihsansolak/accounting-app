package com.cydeo.service.impl;

import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapper;
    private final UserService userService;
    private final InvoiceProductService invoiceProductService;

    @Override
    public InvoiceDTO findById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invoice can not found with id: " + id));

        return mapper.convert(invoice, new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> findAllPurchaseInvoices() {
        String currentlyLoggedInPersonUsername = "abc@email.com";//hardcoded. TODO replace it with SecurityContextHolder when security implemented
        UserDTO loggedInUser = userService.findByUserName(currentlyLoggedInPersonUsername);

        List<Invoice> all = invoiceRepository.findInvoiceByInvoiceTypeAndCompany_Title(InvoiceType.PURCHASE, loggedInUser.getCompany().getTitle());

        return all.stream()
                .map(invoice -> mapper.convert(invoice, new InvoiceDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> findAllSalesInvoices() {
        String currentlyLoggedInPersonUsername = "abc@email.com";//hardcoded. TODO replace it with SecurityContextHolder when security implemented
        UserDTO loggedInUser = userService.findByUserName(currentlyLoggedInPersonUsername);

        List<Invoice> all = invoiceRepository.findInvoiceByInvoiceTypeAndCompany_Title(InvoiceType.SALES, loggedInUser.getCompany().getTitle());

        return all.stream()
                .map(invoice -> mapper.convert(invoice, new InvoiceDTO()))
                .collect(Collectors.toList());
    }

    // invoiceNo, invoiceStatus, invoiceType, date, company cannot be updatable. Update -> ClientVendor
    // id, invoiceStatus, invoiceType, company details should come from DB
    @Override
    public void update(InvoiceDTO foundInvoice, InvoiceDTO invoiceToUpdate) {
        invoiceToUpdate.setId( foundInvoice.getId() );
        invoiceToUpdate.setInvoiceStatus( foundInvoice.getInvoiceStatus() );
        invoiceToUpdate.setInvoiceType( foundInvoice.getInvoiceType() );
        invoiceToUpdate.setCompany(foundInvoice.getCompany() );

        Invoice converted = mapper.convert(invoiceToUpdate, new Invoice());
        invoiceRepository.save( converted );
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        Invoice invoiceToDelete = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NoSuchElementException("Invoice can not found with id: " + invoiceId));

        //delete operation
        invoiceToDelete.setIsDeleted( Boolean.TRUE );

        invoiceRepository.save(invoiceToDelete);
    }

    @Override
    public void approve(Long invoiceId) {
        Invoice invoiceToApprove = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NoSuchElementException("Invoice can not found with id: " + invoiceId));

        invoiceToApprove.setInvoiceStatus(InvoiceStatus.APPROVED);

        invoiceRepository.save(invoiceToApprove);
    }

    //Invoice_No should be auto generated
    //Invoice_Date should be the date which this invoice is created
    @Override
    public InvoiceDTO invoiceCreator(InvoiceType invoiceType) {
        // Get the latest invoice from the database
        Optional<Invoice> latestInvoice = invoiceRepository.findTopByOrderByDateDesc();
        // Generate the new invoice number
        String generatedInvoiceNo = generateNextInvoiceNumber(latestInvoice);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNo( generatedInvoiceNo );
        invoiceDTO.setDate( LocalDate.now() );
        invoiceDTO.setInvoiceType( invoiceType );

        return invoiceDTO;
    }

    private String generateNextInvoiceNumber(Optional<Invoice> lastInvoice) {
        if (lastInvoice.isEmpty()) {
            return "P-000";
        }
        String lastInvoiceNumber = lastInvoice.get().getInvoiceNo();
        int lastNumber = Integer.parseInt(lastInvoiceNumber.substring(2));
        int nextNumber = lastNumber + 1;

        return "P-"+nextNumber;
    }

    @Override
    public InvoiceDTO create(InvoiceDTO invoice) {
        Invoice invoiceToCreate = mapper.convert(invoice, new Invoice());

        //since I need to send Id immediately to UI, I used saveAndFlush() in order to persist the data instantly
        Invoice savedInvoice = invoiceRepository.saveAndFlush(invoiceToCreate);

        return mapper.convert(savedInvoice, new InvoiceDTO());
    }

}
