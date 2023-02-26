package by.tms.tkach.helpdesk.dto.taskQueue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskQueueUpdateDTO {

    private Long id;
    private String name;
    private String description;
    private String departmentName;
}
