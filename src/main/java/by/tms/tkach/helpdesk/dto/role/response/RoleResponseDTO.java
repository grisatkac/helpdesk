package by.tms.tkach.helpdesk.dto.role.response;

import by.tms.tkach.helpdesk.dto.role.RoleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleResponseDTO extends RoleDTO {
    private Long id;
}
