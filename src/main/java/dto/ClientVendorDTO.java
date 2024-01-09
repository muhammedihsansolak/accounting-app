package dto;

import enums.ClientVendorType;
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
    private AddressDto address;
    private boolean hasInvoice;

}
