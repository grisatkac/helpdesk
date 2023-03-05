package by.tms.tkach.helpdesk.dto.department;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class DepartmentUpdateDTO {

    private Long id;
    @Size(min = 5)
    private String name;
    private String description;
    private String headLogin;
}
