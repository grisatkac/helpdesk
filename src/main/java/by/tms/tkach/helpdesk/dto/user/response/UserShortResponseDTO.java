package by.tms.tkach.helpdesk.dto.user.response;

import lombok.Data;

@Data
public class UserShortResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
}
