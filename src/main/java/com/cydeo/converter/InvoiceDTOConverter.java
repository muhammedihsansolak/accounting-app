package com.cydeo.converter;

import com.cydeo.dto.InvoiceDTO;
import com.cydeo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
@RequiredArgsConstructor
public class InvoiceDTOConverter implements Converter<Long, InvoiceDTO> {

    private final InvoiceService invoiceService;

    @Override
    public InvoiceDTO convert(Long source) {
        return invoiceService.findById(source);
    }
}
