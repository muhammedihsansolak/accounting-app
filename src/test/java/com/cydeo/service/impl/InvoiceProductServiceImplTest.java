package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.exception.InvoiceProductNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
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
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceProductServiceImplTest {

    @Mock
    private InvoiceProductRepository repository;
    @Mock
    private MapperUtil mapper;
    @Mock
    private InvoiceServiceImpl invoiceService;

    @InjectMocks
    private InvoiceProductServiceImpl invoiceProductService;

    /*
    ************** findById() **************
     */
    @Test
    void should_throw_exception_when_invoice_product_not_found_with_id(){
        //given
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        //when
        Throwable throwable = catchThrowable(() -> invoiceProductService.findById(id));

        //then
        assertThat(throwable).isInstanceOf(InvoiceProductNotFoundException.class);
    }

    @Test
    void should_execute_mapper_at_least_once_and_should_return_what_is_converted(){
        //given
        Long id = 1L;
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();

        when(repository.findById(id)).thenReturn(Optional.of(invoiceProduct));
        when(mapper.convert(invoiceProduct, new InvoiceProductDTO())).thenReturn(invoiceProductDTO);

        //when
        InvoiceProductDTO returnedInvoiceProduct = invoiceProductService.findById(id);

        //then
        verify(mapper, times(1)).convert(invoiceProduct, invoiceProductDTO);
        assertNotNull(returnedInvoiceProduct);
        assertSame(invoiceProductDTO, returnedInvoiceProduct);
    }

    /*
     ************** findByInvoiceId() **************
     */
    @Test
    void should_find_all_invoice_products_and_calculate_tax_and_total_amount() {
        // Given
        Long invoiceId = 1L;

        InvoiceProduct invoiceProduct1 = new InvoiceProduct();

        InvoiceProduct invoiceProduct2 = new InvoiceProduct();

        List<InvoiceProduct> invoiceProducts = Arrays.asList(invoiceProduct1, invoiceProduct2);

        InvoiceProductDTO invoiceProductDTO1 = new InvoiceProductDTO();
        invoiceProductDTO1.setQuantity(2);
        invoiceProductDTO1.setPrice(BigDecimal.valueOf(100));
        BigDecimal taxAmount1 = BigDecimal.valueOf(20);

        InvoiceProductDTO invoiceProductDTO2 = new InvoiceProductDTO();
        invoiceProductDTO2.setQuantity(1);
        invoiceProductDTO2.setPrice(BigDecimal.valueOf(200));
        BigDecimal taxAmount2 = BigDecimal.valueOf(40);

        when(repository.findByInvoiceId(invoiceId)).thenReturn(invoiceProducts);
        when(mapper.convert(invoiceProduct1, new InvoiceProductDTO())).thenReturn(invoiceProductDTO1);
        when(mapper.convert(invoiceProduct2, new InvoiceProductDTO())).thenReturn(invoiceProductDTO2);
        when(invoiceService.calculateTaxForProduct(invoiceProductDTO1)).thenReturn(taxAmount1);
        when(invoiceService.calculateTaxForProduct(invoiceProductDTO2)).thenReturn(taxAmount2);

        // When
        List<InvoiceProductDTO> result = invoiceProductService.findByInvoiceId(invoiceId);

        // Then
        verify(repository, atLeastOnce()).findByInvoiceId(invoiceId);
        verify(mapper).convert(invoiceProduct1, new InvoiceProductDTO());
        verify(mapper).convert(invoiceProduct2, new InvoiceProductDTO());
        verify(invoiceService).calculateTaxForProduct(invoiceProductDTO1);
        verify(invoiceService).calculateTaxForProduct(invoiceProductDTO2);

        assertEquals(2, result.size()); // Check the size of the result list
        // Assuming that the total amount is calculated and set in the DTO
         assertEquals(BigDecimal.valueOf(220), invoiceProductDTO1.getTotal()); // Total = 100 * 2 + 20
         assertEquals(BigDecimal.valueOf(240), invoiceProductDTO2.getTotal()); // Total = 200 * 1 + 40
    }



}