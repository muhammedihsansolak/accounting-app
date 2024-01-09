package com.cydeo.dto;

import com.cydeo.enums.ClientVendorType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class ClientVendorDTO {

    private Long id;
    private String clientVendorName;
    private String phone;
    private String website;
    private ClientVendorType clientVendorType;
    private AddressDTO address;
    private boolean hasInvoice;

}
