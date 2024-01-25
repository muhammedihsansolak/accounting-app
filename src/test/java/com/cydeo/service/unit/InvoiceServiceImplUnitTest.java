package com.cydeo.service.unit;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceStatus;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

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

    @ExtendWith(MockitoExtension.class)

    @Test
    public void should_throw_exception_when_invoice_not_found() {

        Mockito.when(invoiceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> invoiceServiceImpl.findById(1L));
        assertThat(throwable).isInstanceOf(InvoiceNotFoundException.class);
        assertThat(throwable).hasMessage("Invoice can not found with id: " + 1L);

    }

    @Test
    void should_find_invoice_by_id_and_calculate_total() { //TODO still it is not working.
        // Given - Preparation
        Long invoiceId = 1L;
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setInvoiceNo("Inv-001");

        // Create mock InvoiceProductDTO instances with non-null values for price and quantity
        InvoiceProductDTO invoiceProductDTO1 = new InvoiceProductDTO();
        invoiceProductDTO1.setQuantity(2);
        invoiceProductDTO1.setPrice(BigDecimal.valueOf(100));

        InvoiceProductDTO invoiceProductDTO2 = new InvoiceProductDTO();
        invoiceProductDTO2.setQuantity(1);
        invoiceProductDTO2.setPrice(BigDecimal.valueOf(200));

        List<InvoiceProductDTO> invoiceProductDTOList = Arrays.asList(invoiceProductDTO1, invoiceProductDTO2);

        // Mock behavior for the invoiceProductService
        when(invoiceProductServiceImpl.findByInvoiceId(invoiceId)).thenReturn(invoiceProductDTOList);

        BigDecimal withoutTax = BigDecimal.TEN;
        BigDecimal tax = BigDecimal.valueOf(5);

        // Mock behavior for calculateTotalPriceWithoutTax and calculateTax methods
        when(invoiceServiceImpl.calculateTotalPriceWithoutTax(invoiceProductDTOList)).thenReturn(withoutTax);
        when(invoiceServiceImpl.calculateTax(invoiceProductDTOList)).thenReturn(tax);

        // Mock behavior for the invoiceRepository
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(invoiceId);

        // Mock behavior for the mapper
        when(mapper.convert(invoice, new InvoiceDTO())).thenReturn(invoiceDTO);

        // When - Action
        InvoiceDTO result = invoiceServiceImpl.findById(invoiceId);
        // Then - Assertion/Verification
        assertEquals(invoiceId, result.getId());
        assertEquals(withoutTax, result.getPrice());
        assertEquals(tax, result.getTax());
        assertEquals(withoutTax.add(tax), result.getTotal());

    }


    @Test
    void should_find_All_Invoices() {  //TODO it is not working..

        // Given

        UserDTO loggedInUser = new UserDTO();
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setTitle("Test Company");
        loggedInUser.setCompany(companyDTO);

        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);

        Invoice invoice2 = new Invoice();
        invoice2.setId(2L);

        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2);

        when(securityServiceImpl.getLoggedInUser()).thenReturn(loggedInUser);

        when(invoiceRepository.findInvoiceByInvoiceTypeAndCompany_TitleAndIsDeletedOrderByInvoiceNoDesc(
                any(InvoiceType.class), anyString(), eq(false))).thenReturn(invoiceList);

        when(invoiceProductServiceImpl.findByInvoiceId(anyLong())).thenReturn(Arrays.asList(new InvoiceProductDTO()));

        // When
        List<InvoiceDTO> result = invoiceServiceImpl.findAllInvoices(InvoiceType.SALES);

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        // Verify that the methods were called
        verify(securityServiceImpl, times(1)).getLoggedInUser();
        verify(invoiceRepository, times(1)).findInvoiceByInvoiceTypeAndCompany_TitleAndIsDeletedOrderByInvoiceNoDesc(
                any(InvoiceType.class), anyString(), eq(false));
        verify(invoiceProductServiceImpl, times(2)).findByInvoiceId(anyLong());
    }


    @Test
    void should_calculate_tax_for_given_invoiceProductList() { //TODO it is not working

        // Given - Preparation
        InvoiceProductDTO invoiceProductDTO1 = new InvoiceProductDTO();
        invoiceProductDTO1.setQuantity(2);
        invoiceProductDTO1.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO1.setTax(10);

        InvoiceProductDTO invoiceProductDTO2 = new InvoiceProductDTO();
        invoiceProductDTO2.setQuantity(1);
        invoiceProductDTO2.setPrice(BigDecimal.valueOf(200));
        invoiceProductDTO2.setTax(10);


        List<InvoiceProductDTO> invoiceProductDTOList = Arrays.asList(invoiceProductDTO1, invoiceProductDTO2);

        // Mock behavior for calculateTax method
        when(invoiceServiceImpl.calculateTax(anyList())).thenCallRealMethod();

        // When - Action
        BigDecimal tax = invoiceServiceImpl.calculateTax(invoiceProductDTOList);

        // Then - Assertion/Verification
        assertEquals(BigDecimal.valueOf(15), tax); // Assuming a tax rate of 5%
    }


    @Test
    void should_calculate_tax_for_given_Product() {

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO.setTax(10);
        invoiceProductDTO.setQuantity(5);

        BigDecimal taxAmount = invoiceServiceImpl.calculateTaxForProduct(invoiceProductDTO);

        assertEquals(BigDecimal.valueOf(50), taxAmount);

    }

    @Test
    void should_throw_exception_when_calculating_negative_tax_for_given_Product() {
        // Given
        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setPrice(BigDecimal.valueOf(100));
        invoiceProductDTO.setTax(-10); // negative tax percentage
        invoiceProductDTO.setQuantity(2);

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceServiceImpl.calculateTaxForProduct(invoiceProductDTO);
        });

        // Then
        assertEquals("Tax amount cannot be negative!", exception.getMessage());
    }

    @Test
    void calculate_total_price_without_Tax() {
        // Given
        InvoiceProductDTO product1 = new InvoiceProductDTO();
        product1.setPrice(BigDecimal.valueOf(100)); // Set the price of the first product to 100
        product1.setQuantity(2); // Set the quantity of the first product to 2

        InvoiceProductDTO product2 = new InvoiceProductDTO();
        product2.setPrice(BigDecimal.valueOf(50)); // Set the price of the second product to 50
        product2.setQuantity(3); // Set the quantity of the second product to 3

        List<InvoiceProductDTO> invoiceProductDTOList = Arrays.asList(product1, product2); // Create a list of InvoiceProductDTOs

        // When
        BigDecimal totalPriceWithoutTax = invoiceServiceImpl.calculateTotalPriceWithoutTax(invoiceProductDTOList); // Calculate the total price without tax

        // Then
        assertEquals(BigDecimal.valueOf(350), totalPriceWithoutTax); // Verify that the calculated total price without tax is 350 (100 * 2 + 50 * 3 = 350)
    }

    @Test
    void should_update_invoice() {  // TODO it is not working

        // Given
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setTitle("test company");
        InvoiceDTO foundInvoice = new InvoiceDTO();
        foundInvoice.setId(1L);
        foundInvoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        foundInvoice.setInvoiceType(InvoiceType.PURCHASE);
        foundInvoice.setCompany(companyDTO);

        InvoiceDTO invoiceToUpdate = new InvoiceDTO();
        invoiceToUpdate.setId(1L);
        invoiceToUpdate.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceToUpdate.setInvoiceType(InvoiceType.SALES);
        invoiceToUpdate.setCompany(companyDTO);

        // When
        when(mapper.convert(foundInvoice, new InvoiceDTO())).thenReturn(invoiceToUpdate);

        invoiceServiceImpl.update(foundInvoice, invoiceToUpdate);

        // Then
        verify(invoiceRepository, times(1)).save(any(Invoice.class));

    }


    @Test
    void delete() { // TODO it doesnt work
        // Given
        Long invoiceId = 1L;
        Invoice invoiceToDelete = new Invoice();
        invoiceToDelete.setId(invoiceId);

        // Mock the behavior of the repository
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoiceToDelete));

        // Mock the behavior of the mapper
        when(mapper.convert(invoiceToDelete, new InvoiceDTO())).thenReturn(new InvoiceDTO());

        // When
        invoiceServiceImpl.deleteInvoice(invoiceId);

        // Then
        verify(invoiceRepository, times(1)).findById(invoiceId);
        verify(invoiceRepository, times(1)).save(invoiceToDelete);
        verify(invoiceProductServiceImpl, times(1)).deleteByInvoice(any(InvoiceDTO.class));
    }

    @Test
    void should_approve_invoice() { //TODO I couldnt solve
        // Given
        Long invoiceId = 1L;

        Invoice invoiceToApprove = new Invoice();
        invoiceToApprove.setId(invoiceId);
        invoiceToApprove.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceToApprove.setInvoiceType(InvoiceType.SALES);


    }


}
