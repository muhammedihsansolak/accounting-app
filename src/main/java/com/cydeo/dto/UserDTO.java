package com.cydeo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
    private String firstname;
    private String lastname;
    private String phone;
    private RoleDTO role;
    private CompanyDTO company;
    private boolean enabled;

    private boolean isOnlyAdmin; //(should be true if this user is only admin of any company.) I will write in business logic part

}
