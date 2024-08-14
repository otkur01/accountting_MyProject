package com.cydeo.dto;

import com.cydeo.annotation.PasswordMatches;
import com.cydeo.annotation.ValidPassword;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class UserDto {

    private Long id;
    @Column(unique = true)
    @NotBlank(message = "Email is required field.")
    @Email(message = "Email should be in a valid format.")
    private String username;

    @NotBlank(message = "Confirm Password is required.")
    @ValidPassword
    private String password;


    private String confirmPassword;

    @NotBlank(message = "First Name is required field.")
    @NotNull(message = "First Name is required field.")
    @Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters long.")
    private String firstname;

    @NotBlank(message = "Last Name is required field.")
    @Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters long.")
    private String lastname;

    @NotNull(message = "Please select a Role")
    private RoleDto role;

    @NotBlank(message = "Phone Number is a required field ")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "may be in any valid phone number format.")
    private String phone;


    @NotNull(message = "Please select a Company.")
    private CompanyDto company;


    private boolean isOnlyAdmin;

    public void setPassword(String password) {
        this.password = password;
        checkConfirmPassword();
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkConfirmPassword();
    }

    private void checkConfirmPassword() {
        if (password != null && !password.equals(confirmPassword)) {
            this.confirmPassword = null;
        }
    }

}
