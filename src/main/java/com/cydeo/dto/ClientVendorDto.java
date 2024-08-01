package com.cydeo.dto;

import com.cydeo.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientVendorDto {

    private Long id;

    @NotBlank(message = "Company Name is required field.")
    @Size(min = 2, max = 50, message = "Company Name must be between 2 and 50 characters long.")
    private String clientVendorName;

    @NotBlank(message = "Phone Number is a required field ")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "may be in any valid phone number format.")
    private String phone;

    @Pattern(regexp = "^(https?://)?([a-zA-Z0-9.-]{+}).([a-zA-Z] {2,})(:[0-9])?(/[^/])*$", message = "Website should have a valid format")
    private String website;
    @NotBlank(message = "Please select type")
    private ClientVendorType clientVendorType;

    private AddressDto address;

    private CompanyDto company;

    private boolean hasInvoice;
}
