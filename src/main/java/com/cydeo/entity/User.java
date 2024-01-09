package com.cydeo.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Column(unique = true)
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role; // many-to-one / will be seen under "role_id" column on the "users" table

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company; // many-to-one / will be seen under "company_id" column on the "users" table

}
