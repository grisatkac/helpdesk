package by.tms.tkach.helpdesk.dto.taskQueue.response;

import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TaskQueueDetailsResponse {
    private Long id;
    private String name;
    private String description;
    private DepartmentShortResponseDTO department;
}
