package com.cydeo.converter;
import com.cydeo.dto.ClientVendorDTO;
import com.cydeo.entity.ClientVendor;
import org.modelmapper.Converter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public abstract class ClientVendorDTOConverter implements Converter<String, ClientVendorDTO> {

    public static ClientVendor convert(ClientVendorDTO clientVendorDTO) {
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(clientVendorDTO.getId());
        return clientVendor;
    }

}



