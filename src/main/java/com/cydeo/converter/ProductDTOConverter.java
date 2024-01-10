package com.cydeo.converter;

import com.cydeo.dto.ProductDTO;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
@RequiredArgsConstructor
public class ProductDTOConverter implements Converter <Long, ProductDTO> {

    private final ProductService productService;

    @Override
    public ProductDTO convert(Long source) {
        return productService.findById(source);
    }
}
