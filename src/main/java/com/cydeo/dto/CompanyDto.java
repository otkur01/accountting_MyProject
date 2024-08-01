package com.cydeo.dto;

import com.cydeo.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {


    private Long id;

    @NotBlank(message = "Title is a required field")
    @Size(min = 2, max = 100, message = "Title should be 2-100 characters long")
    private String title;

    @NotBlank(message = "Phone Number is a required field ")
    @Pattern(regexp = "^\\+?\\d{1,3}?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "may be in any valid phone number format.(ex: +1 (957) 463-7174)")
    private String phone;
    @Pattern(regexp =  "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", message = "Website should have a valid format")
    private String website;
    private AddressDto address;
    private CompanyStatus companyStatus;

}
