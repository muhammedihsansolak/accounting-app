package com.cydeo.converter;

import com.cydeo.dto.ProductDTO;
import com.cydeo.service.ProductService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ProductDTOConverter implements Converter <String, ProductDTO> {

    private final ProductService productService;

    public ProductDTOConverter(@Lazy ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDTO convert(String source) {
        if (source.equals("")) return null;
        Long id = Long.valueOf(source);
        return productService.findById(id);
    }
}
