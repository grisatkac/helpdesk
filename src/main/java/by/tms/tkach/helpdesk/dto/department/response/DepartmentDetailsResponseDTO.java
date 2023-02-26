package by.tms.tkach.helpdesk.dto.department.response;

import by.tms.tkach.helpdesk.dto.user.response.UserShortResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class DepartmentDetailsResponseDTO {
    private Long id;
    private String name;
    private String description;
    private UserShortResponseDTO head;
}
