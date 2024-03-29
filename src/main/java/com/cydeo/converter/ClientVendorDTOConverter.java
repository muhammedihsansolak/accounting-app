package com.cydeo.converter;

import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.service.ClientVendorService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ClientVendorDTOConverter implements Converter<String, ClientVendorDTO> {

   private final ClientVendorService clientVendorService;

    protected ClientVendorDTOConverter(@Lazy ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    @Override
    public ClientVendorDTO convert(String  source){
        if(source.equals("")) return null;
        Long id = Long.valueOf(source);
       return clientVendorService.findById(id);
   }

}



