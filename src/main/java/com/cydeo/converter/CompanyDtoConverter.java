package com.cydeo.converter;

import com.cydeo.dto.CompanyDTO;
import com.cydeo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
@RequiredArgsConstructor
public class CompanyDtoConverter implements Converter<Long, CompanyDTO> {
    private final CompanyService companyService;
    @Override
    public CompanyDTO convert(Long companyId) {
        if (companyId == null || companyId.equals("")){
            return null;
        }
        return companyService.findById(companyId);
    }
}
