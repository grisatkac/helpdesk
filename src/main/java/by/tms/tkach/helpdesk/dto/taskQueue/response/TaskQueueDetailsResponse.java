package by.tms.tkach.helpdesk.dto.taskQueue.response;

import by.tms.tkach.helpdesk.dto.department.response.DepartmentShortResponseDTO;
import lombok.Data;

@Data
public class TaskQueueDetailsResponse {
    private Long id;
    private String name;
    private String description;
    private DepartmentShortResponseDTO department;
}
