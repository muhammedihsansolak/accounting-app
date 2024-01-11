package com.cydeo.entity;


import com.cydeo.enums.ClientVendorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients_vendors")
public class ClientVendor extends BaseEntity {

    private String clientVendorName;
    private String phone;
    private String website;




    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")

    private Address address;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


}
