package by.tms.tkach.helpdesk.dto.auth;

import by.tms.tkach.helpdesk.dto.department.response.DepartmentResponseDTO;
//import by.tms.tkach.helpdesk.dto.role.response.RoleResponseDTO;
import lombok.*;

@Getter
@Setter
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class AuthUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;
    //private RoleResponseDTO role;
    private String role;
    private DepartmentResponseDTO department;
}
