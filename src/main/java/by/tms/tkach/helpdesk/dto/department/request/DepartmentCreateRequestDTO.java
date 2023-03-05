package by.tms.tkach.helpdesk.dto.department.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class DepartmentCreateRequestDTO {

    @NotEmpty(message = "Department name can't be empty")
    @Size(min = 5, message = "Minimum characters of department name is 5")
    private String name;
    @Size(max = 1000, message = "Description should contain maximum from 1000 characters")
    private String description;
}
