package by.tms.tkach.helpdesk.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequestDTO {

    @NotEmpty(message = "First name can't be empty")
    @Size(min = 5, max = 20, message = "First can be between 5 and 20")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 5, max = 20, message = "First can be between 5 and 20")
    private String lastName;
    @NotEmpty(message = "login can't be empty")
    @Size(min = 5, message = "Login must contain minimum 5 characters")
    private String login;
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 5, message = "Password must contain minimum from characters")
    private String password;
    @NotEmpty(message = "Email can't be empty")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Incorrect email")
    private String email;
}
