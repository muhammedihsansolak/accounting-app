package com.cydeo.converter;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CompanyDtoConverter implements Converter<String, CompanyDTO> {
    private final CompanyService companyService;

    @Override
    public CompanyDTO convert(String source) {
        Long of = Long.valueOf(source);
        return companyService.findById(of);
    }
}

