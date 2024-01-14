package com.cydeo.service.impl;

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
import com.cydeo.service.SecurityService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapper;
    private final UserService userService;
    private final SecurityService securityService;
    private final InvoiceProductService invoiceProductService;

    @Override
    public InvoiceDTO findById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invoice can not found with id: " + id));
        InvoiceDTO invoiceDTO = mapper.convert(invoice, new InvoiceDTO());

        List<InvoiceProductDTO> invoiceProductDTOList = invoiceProductService.findByInvoiceId(invoiceDTO.getId());

        BigDecimal withoutTax = calculateTotalPriceWithoutTax(invoiceProductDTOList);
        BigDecimal tax = calculateTax(invoiceProductDTOList);

        invoiceDTO.setPrice(withoutTax);
        invoiceDTO.setTax(tax);
        invoiceDTO.setTotal(withoutTax.add(tax));

        return invoiceDTO;
    }

    @Override
    public List<InvoiceDTO> findAllInvoices(InvoiceType invoiceType) {
        String currentlyLoggedInPersonUsername = securityService.getLoggedInUser().getUsername();
        UserDTO loggedInUser = userService.findByUsername(currentlyLoggedInPersonUsername);

        List<Invoice> all = invoiceRepository.findInvoiceByInvoiceTypeAndCompany_Title(invoiceType, loggedInUser.getCompany().getTitle());

        List<InvoiceDTO> invoiceDTOList = all.stream()
                .map(invoice -> mapper.convert(invoice, new InvoiceDTO()))
                .collect(Collectors.toList());

        invoiceDTOList = invoiceDTOList.stream()
                .map(invoice -> {
                    List<InvoiceProductDTO> invoiceProductDTOList = invoiceProductService.findByInvoiceId(invoice.getId());
                    BigDecimal totalPriceWithoutTax =  calculateTotalPriceWithoutTax(invoiceProductDTOList);
                    BigDecimal taxAmount =  calculateTax(invoiceProductDTOList);

                    invoice.setPrice( totalPriceWithoutTax );
                    invoice.setTax( taxAmount );
                    invoice.setTotal( totalPriceWithoutTax.add( taxAmount ) );

                    return invoice;
                })
                .collect(Collectors.toList());

        return invoiceDTOList;
    }

    private static final BigDecimal PERCENTAGE_DIVISOR = BigDecimal.valueOf(100);

    private BigDecimal calculateTax(List<InvoiceProductDTO> invoiceProductDTOList) {
        BigDecimal sum = invoiceProductDTOList.stream()
                .map(this::calculateTaxForProduct)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tax cannot be negative!");
        }

        return sum;
    }

    @Override
    public BigDecimal calculateTaxForProduct(InvoiceProductDTO invoiceProductDTO) {
        BigDecimal price = invoiceProductDTO.getPrice();
        BigDecimal taxPercentage = BigDecimal.valueOf(invoiceProductDTO.getTax());
        Integer quantity = invoiceProductDTO.getQuantity();

        BigDecimal taxAmount = price.multiply(taxPercentage).divide(PERCENTAGE_DIVISOR, RoundingMode.CEILING).multiply(BigDecimal.valueOf(quantity));

        if (taxAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tax amount cannot be negative!");
        }

        return taxAmount;
    }


    private BigDecimal calculateTotalPriceWithoutTax(List<InvoiceProductDTO> invoiceProductDTOList) {
        BigDecimal sum = invoiceProductDTOList.stream()
                .map(invoiceProductDTO ->
                        (invoiceProductDTO.getPrice()).multiply(BigDecimal.valueOf(invoiceProductDTO.getQuantity()) ))//total price amount without tax
                .reduce(BigDecimal.ZERO, BigDecimal::add );

        return sum;

    }

    // invoiceNo, invoiceStatus, invoiceType, date, company cannot be updatable. Update -> ClientVendor
    // id, invoiceStatus, invoiceType, company details should come from DB
    @Override
    public void update(InvoiceDTO foundInvoice, InvoiceDTO invoiceToUpdate) {
        invoiceToUpdate.setId( foundInvoice.getId() );
        invoiceToUpdate.setInvoiceStatus( foundInvoice.getInvoiceStatus() );
        invoiceToUpdate.setInvoiceType( foundInvoice.getInvoiceType() );
        invoiceToUpdate.setCompany( foundInvoice.getCompany() );

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
    public InvoiceDTO invoiceCreator(InvoiceType invoiceType, String companyTitle) {
        // Get the latest invoice from the database which belongs to that company
        Optional<Invoice> latestInvoice = invoiceRepository.findTopByCompany_TitleOrderByDateDesc(companyTitle);
        // Generate the new invoice number
        String generatedInvoiceNo = generateNextInvoiceNumber(latestInvoice, invoiceType);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNo( generatedInvoiceNo );
        invoiceDTO.setDate( LocalDate.now() );
        invoiceDTO.setInvoiceType( invoiceType );

        return invoiceDTO;
    }

    private String generateNextInvoiceNumber(Optional<Invoice> lastInvoice, InvoiceType invoiceType) {
        if (!lastInvoice.isPresent()) {
            return invoiceType.getValue().charAt(0) + "-000";
        }

        String lastInvoiceNumber = lastInvoice.get().getInvoiceNo();
        int lastNumber = Integer.parseInt(lastInvoiceNumber.substring(2));
        int nextNumber = lastNumber + 1;

        return invoiceType.getValue().charAt(0) + "-" + String.format("%03d", nextNumber);
    }

    @Override
    public InvoiceDTO create(InvoiceDTO invoice) {
        Invoice invoiceToCreate = mapper.convert(invoice, new Invoice());

        //since I need to send invoice id immediately to UI, I used saveAndFlush() to persist data instantly
        Invoice savedInvoice = invoiceRepository.saveAndFlush(invoiceToCreate);

        return mapper.convert(savedInvoice, new InvoiceDTO());
    }

}
