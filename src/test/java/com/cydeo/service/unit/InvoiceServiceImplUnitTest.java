package com.cydeo.service.unit;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.dto.InvoiceDTO;
import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Company;
import com.cydeo.entity.Invoice;
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
 public void should_throw_exception_when_invoice_not_found(){

    Mockito.when(invoiceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
    Throwable throwable = catchThrowable(() -> invoiceServiceImpl.findById(1L));
    assertThat(throwable).isInstanceOf(InvoiceNotFoundException.class);
    assertThat(throwable).hasMessage("Invoice can not found with id: "+1L);

}





}
