package com.cydeo.converter;

import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.service.InvoiceProductService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class InvoiceProductDTOConverter implements Converter<Long, InvoiceProductDTO> {

    private final InvoiceProductService service;

    public InvoiceProductDTOConverter(@Lazy InvoiceProductService service) {
        this.service = service;
    }

    @Override
    public InvoiceProductDTO convert(Long source) {
        return service.findById(source);
    }

}
