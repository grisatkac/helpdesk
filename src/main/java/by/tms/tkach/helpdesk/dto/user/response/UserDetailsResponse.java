package by.tms.tkach.helpdesk.dto.user.response;

import by.tms.tkach.helpdesk.dto.department.response.DepartmentResponseDTO;
import by.tms.tkach.helpdesk.dto.role.response.RoleResponseDTO;
import lombok.Data;

@Data
public class UserDetailsResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private RoleResponseDTO role;
    private DepartmentResponseDTO department;
}
