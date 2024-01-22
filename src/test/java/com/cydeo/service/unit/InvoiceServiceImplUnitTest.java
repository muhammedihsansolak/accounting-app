package com.cydeo.service.unit;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.InvoiceNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import com.cydeo.service.SecurityService;
import com.cydeo.service.impl.InvoiceProductServiceImpl;
import com.cydeo.service.impl.InvoiceServiceImpl;
import com.cydeo.service.impl.ProductServiceImpl;
import com.cydeo.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImplUnitTest {


    @Mock
    InvoiceRepository invoiceRepository;
    @Mock
    MapperUtil mapper;
    @Mock
    SecurityServiceImpl securityServiceImpl;
    @Mock
    InvoiceProductServiceImpl invoiceProductServiceImpl;
    @Mock
    ProductServiceImpl productServiceImpl;

    @InjectMocks
    InvoiceServiceImpl invoiceServiceImpl;

    @Test
    public void should_throw_exception_when_invoice_not_found() {

        Mockito.when(invoiceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> invoiceServiceImpl.findById(1L));
        assertThat(throwable).isInstanceOf(InvoiceNotFoundException.class);
        assertThat(throwable).hasMessage("Invoice can not found with id: " + 1L);

    }

    @Test
    void should_find_invoice_by_id_and_calculate_total() {
        // Given - Preparation
        Long invoiceId = 1L;
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setInvoiceNo("Inv-001");

        // Mock behavior for the invoiceRepository
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoiceId);

        // Mock behavior for the mapper
        when(mapper.convert(any(), any())).thenReturn(invoiceDTO);

        //TODO fix NullPointerException !
        List<InvoiceProductDTO> invoiceProductDTOList = new ArrayList<>();
        invoiceProductDTOList.add(new InvoiceProductDTO());
        // Mock behavior for the invoiceProductService
        when(invoiceProductServiceImpl.findByInvoiceId(invoiceId)).thenReturn(invoiceProductDTOList);

        BigDecimal withoutTax = BigDecimal.TEN;
        BigDecimal tax = BigDecimal.valueOf(5);
        // Mock behavior for calculateTotalPriceWithoutTax and calculateTax methods
        when(invoiceServiceImpl.calculateTotalPriceWithoutTax(invoiceProductDTOList)).thenReturn(withoutTax);
        when(invoiceServiceImpl.calculateTax(invoiceProductDTOList)).thenReturn(tax);

        // When - Action
        InvoiceDTO result = invoiceServiceImpl.findById(invoiceId);
        // Then - Assertion/Verification
        assertEquals(invoiceId,result.getId());
        assertEquals(withoutTax,result.getPrice());
        assertEquals(tax,result.getTax());
        assertEquals(withoutTax.add(tax),result.getTotal());

    }


    @Test
    void should_find_All_Invoices(){

        UserDTO loggedInUser = new UserDTO();
        CompanyDTO companyDTO =new CompanyDTO();
        companyDTO.setTitle("Test Company");
        loggedInUser.setCompany(companyDTO);

        List<Invoice> invoiceList = Arrays.asList(new Invoice(),new Invoice());
        List<InvoiceProductDTO> invoiceProductDTOList = Arrays.asList(new InvoiceProductDTO());

        // Mock behavior of securityService.getLoggedInUser() to return the sample user
        when(securityServiceImpl.getLoggedInUser()).thenReturn(loggedInUser);
        // Mock behavior of invoiceRepository.findInvoiceByInvoiceTypeAndCompany_TitleAndIsDeletedOrderByInvoiceNoDesc()
        when(invoiceRepository.findInvoiceByInvoiceTypeAndCompany_TitleAndIsDeletedOrderByInvoiceNoDesc(any(),any(), anyBoolean())).thenReturn(invoiceList); //TODO this line doesnt work..

        // Mock behavior of invoiceProductService.findByInvoiceId() to return the sample list of invoice products
        when(invoiceProductServiceImpl.findByInvoiceId(loggedInUser.getId())).thenReturn(invoiceProductDTOList);

        // Mock behavior of mapper.convert() for each invoice
        when(mapper.convert(any(),InvoiceDTO.class));

        // Call the method to test  //TODO How I can add InvoiceType.PURCHASE ???
        List<InvoiceDTO> result = invoiceServiceImpl.findAllInvoices(InvoiceType.SALES);

        assertNotNull(result);

    }






}
