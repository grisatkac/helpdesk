package by.tms.tkach.helpdesk.dto.department;

import lombok.Data;

@Data
public class DepartmentUpdateDTO {

    private Long id;
    private String name;
    private String description;
    private String headLogin;
}
