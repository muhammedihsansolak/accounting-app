package com.cydeo.converter;

import com.cydeo.dto.InvoiceProductDTO;
import com.cydeo.service.InvoiceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
@RequiredArgsConstructor
public class InvoiceProductDTOConverter implements Converter<Long, InvoiceProductDTO> {

    private final InvoiceProductService service;

    @Override
    public InvoiceProductDTO convert(Long source) {
        return service.findById(source);
    }

}
