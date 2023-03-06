package by.tms.tkach.helpdesk.dto.department.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class DepartmentShortResponseDTO {
    private Long id;
    private String name;
}
