package by.tms.tkach.helpdesk.dto.user;

//import by.tms.tkach.helpdesk.entities.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserUpdateDTO {
    private Long id;
    @NotEmpty(message = "First name can't be empty")
    @Size(min = 5, max = 20, message = "First can be between 5 and 20")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 5, max = 20, message = "First can be between 5 and 20")
    private String lastName;
    @NotEmpty(message = "Email can't be empty")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Incorrect email")
    private String email;
    @NotEmpty(message = "login can't be empty")
    @Size(min = 5, message = "Login must contain minimum 5 characters")
    private String login;
    //private Role role;
    private String role;
    @NotEmpty(message = "Department name can't be empty")
    private String departmentName;
}
